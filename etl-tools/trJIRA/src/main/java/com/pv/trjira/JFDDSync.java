package com.pv.trjira;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class JFDDSync {
	// JIRA Statuses
	public static final String JIRA_STATUS_OPEN = "1";
	public static final String JIRA_STATUS_IN_DESIGN = "10000";
	public static final String JIRA_STATUS_DESIGN_INSPECTION = "10001";
	public static final String JIRA_STATUS_CODE_UNIT_TEST = "10002";
	public static final String JIRA_STATUS_CODE_INSPECTION = "10003";
	public static final String JIRA_STATUS_RESOLVED = "5";
	public static final String JIRA_STATUS_CLOSED = "6";

	
	static MySQLStmts mySQLStmts = null;
	public static String prop;
	public static String defaultEstimate;
	public static String baseUrl;
	public static String jiraLoginUsername;
	public static String jiraLoginPassword;
	public static String jiraFilterID;
	public static String dbUsername;
	public static String dbPassword;
	public static String dbURL;
	public static String dbDriver;
	public static Log log = null;
	public static Properties applicationProps;

	public JFDDSync() throws Exception {
		prop = System.getProperty("jfdd.properties");

		log = LogFactory.getLog(JFDDSync.class);
		log.info("Using properties file: " + prop);

		applicationProps = new Properties();
		FileInputStream appStream = new FileInputStream(prop);
		applicationProps.load(appStream);
		appStream.close();

		defaultEstimate = applicationProps.getProperty("default.estimate", "432000");
		dbUsername = applicationProps.getProperty("db.username", "admin");
		dbPassword= applicationProps.getProperty("db.password", "");
		dbURL = applicationProps.getProperty("db.url", "jdbc:mysql://localhost:3306/fddpma");
		dbDriver = applicationProps.getProperty("db.driver", "com.mysql.jdbc.Driver");
		
	}

	// ( status |step number| pct effort for step| accumulative % effort )
	public static final WorkflowStep[] milestones = { new WorkflowStep(JIRA_STATUS_IN_DESIGN, 1, 0.01, 0.01),
			new WorkflowStep(JIRA_STATUS_DESIGN_INSPECTION, 2, 0.40, 0.41),
			new WorkflowStep(JIRA_STATUS_CODE_UNIT_TEST, 3, 0.03, 0.44),
			new WorkflowStep(JIRA_STATUS_CODE_INSPECTION, 4, 0.45, 0.89),
			new WorkflowStep(JIRA_STATUS_RESOLVED, 5, 0.10, 0.99), new WorkflowStep(JIRA_STATUS_CLOSED, 6, 0.01, 1) };
	
	/**
	 * This relies on a filter in JIRA that looks at records since the last time this job was run. It picks up any
	 * closed JIRA items. However we don't want to keep on picking them up, hence a date filter. E.g. last 2 days.
	 * 
	 * @throws ConfigException
	 * @throws java.rmi.RemoteException
	 * @throws MalformedURLException
	 * @throws SQLException
	 */
	public static void syncJiraToFddpma(FeatureIssue[] issues) throws ConfigException,			SQLException {

		log.info("Starting JIRA to FDDPMA Synchronisation");
		MySQLStmts mySQLStmts = new MySQLStmts();
		Feature feature = null;
		int projectId = -1;

		final Integer FEATURE_NOT_FOUND = 0;

		// Loop through each Feature found in JIRA
		for (int i = 0; i < issues.length; i++) {
			FeatureIssue issue = issues[i];
			WorkflowStep step = getCurrentMilestoneStep(issue);

			// if the workflow step is not mapped, then forget about it
			if (step == null && (issue.getStatus().compareTo(JIRA_STATUS_OPEN) != 0))
				continue;

			projectId = mySQLStmts.getProject(issue.getProject());
			if (projectId == -1)
				throw new ConfigException(
						"Can't find the project key. Needs to be added to FDDPMA_PROJECT.JIRA_KEY. E.g. 'MYPROJECT'");

			// find the related feature in FDDPMA
			feature = mySQLStmts.getFeature(issue.getKey());

			if (issue.getStatus().compareTo(JIRA_STATUS_OPEN) != 0) {
				// assume the workflow has started in JIRA

				if (feature.getFeatureId() == FEATURE_NOT_FOUND) {
					// create feature in FDDPMA and update the Milestones
					Feature newFeature = createNewFeature(projectId, issue);
					createMilestonesInFDDPMA(issue, step, newFeature);

				} else {
					// the workflow is in FDDPMA, now update the actual dates
					List<FeatureMilestone> listMilestones = mySQLStmts.getMilstonesForFeature(feature.getFeatureId());

					if (listMilestones.size() == 0) {
						// we need to initialise the milestones.
						createMilestonesInFDDPMA(issue, step, feature);

					} else {
						// milestones exist, just need to update the actual dates
						listMilestones = setActualDates(issue, listMilestones, step);

						// update milestone records in FDDPMA (need to capture dates we might have missed)
						for (Iterator<FeatureMilestone> iterator = listMilestones.iterator(); iterator.hasNext();) {
							FeatureMilestone milestoneDate = (FeatureMilestone) iterator.next();
							mySQLStmts.updateMilestoneActualDate(milestoneDate, feature);
						}
					}
				}
			} else {
				// just create the feature
				if (feature.getFeatureId() == FEATURE_NOT_FOUND)
					createNewFeature(projectId, issue);
			}

			log.info(issue.getKey() + ": " + issue.getSummary() + " status: " + issue.getStatus()
					+ " originalEstimate: " + issue.getOriginalEstimate());
		}
		log.info("Completed JIRA to FDDPMA Synchronisation");
	}

	/**
	 * Create a feature in FDDPMA that's already started in JIRA. I.e. we need to back date the information in FDDPMA.
	 * 
	 * @param token
	 * @param filterId
	 * @param issue
	 * @param projectId
	 * @throws SQLException
	 * @throws ConfigException
	 */
	public static void createMilestonesInFDDPMA(FeatureIssue issue, WorkflowStep step, Feature feature)
			throws SQLException, ConfigException {

		// prepare a new feature

		// set the planned dates
		List<FeatureMilestone> listMilestones = setPlannedDates(issue, getEstimate(issue));

		// set the actual dates
		listMilestones = setActualDates(issue, listMilestones, step);

		// create new milestone records in FDDPMA
		for (Iterator<FeatureMilestone> iterator = listMilestones.iterator(); iterator.hasNext();) {
			FeatureMilestone milestoneDate = (FeatureMilestone) iterator.next();
			mySQLStmts.insertMilestone(milestoneDate, feature);
		}

	}

	/**
	 * Work out the estimated time to complete this feature.
	 * 
	 * @param issue
	 * @return
	 */
	public static Long getEstimate(FeatureIssue issue) {
		Long estimate = 0L; // this is how long we think it will take to code the feature

		// work out the estimated completion time
		estimate = issue.getOriginalEstimate();
		if (estimate == null) {
			estimate = issue.getEstimate(); // use the estimated day if one wasn't provided
			if (estimate == null) {
				estimate = new Long(defaultEstimate); // default to 5 days
			}
		}
		return estimate;
	}

	/**
	 * Creates a new feature in FDDPMA
	 * 
	 * @param projectId
	 * @param issue
	 * @return Feature
	 * @throws SQLException
	 */
	public static Feature createNewFeature(int projectId, FeatureIssue issue) throws SQLException {

		Feature newFeature = new Feature();
		newFeature.setProjectId(projectId);
		newFeature.setFeatureName(issue.getSummary());
		newFeature.setCancelled(0);
		newFeature.setJiraId(issue.getKey());

		// create a new feature in FDDPMA
		int featureId = mySQLStmts.insertFeature(newFeature);
		newFeature.setFeatureId(featureId);

		return newFeature;
	}

	/*
	 * Return the FDDPMA Milestone ID. e.g. 1=Walkthrough, 2=Design etc.
	 */
	public static WorkflowStep getCurrentMilestoneStep(FeatureIssue issue) throws ConfigException {

		// find the position of the JIRA item in the workflow
		WorkflowStep step = null;
		for (int i = 0; i < milestones.length; i++) {
			WorkflowStep wfstep = milestones[i];
			if (wfstep.getJiraStep().equals(issue.getStatus())) {
				step = milestones[i];
				break;
			}
		}
		return step;
	}

	/**
	 * Work out the Planned dates for the given estimate and start date. It also caters for JIRA items that have already
	 * started and may be part way through a workflow. In this instance the function takes the last updated date for the
	 * feature plus the estimated time for the whole job and the position of the feature in the workflow process. It
	 * then works out the probable start date.
	 * 
	 * This is called only when creating new features.
	 * 
	 * @param issue
	 * @param estimate
	 * @return
	 * @throws ConfigException
	 */
	public static List<FeatureMilestone> setPlannedDates(FeatureIssue issue, Long estimate) throws ConfigException {
		final int FDD_WALKTHROUGH = 0;

		List<FeatureMilestone> list = new ArrayList<FeatureMilestone>();
		FeatureMilestone fmd = null;

		/**
		 * PLANNED DATES
		 */
		// find the start date so we can determine the planned dates
		// we do this in case this is a new jira item that has progressed through the workflow
		// find the position of the JIRA item in the workflow
		// Planned Date - walk through
		Calendar pcd = Calendar.getInstance();
		pcd.setTime(issue.getUpdated().getTime()); // get the time this JIRA item was updated

		// Find milestone position of this JIRA item in the workflow
		WorkflowStep step = getCurrentMilestoneStep(issue);

		// Do our best to work out the start (walkthrough) date
		pcd.add(Calendar.SECOND, (int) ((estimate * step.getPctTotalEffort()) / -1));

		// update each workflow step for this issue with planned dates
		for (int i = 0; i < milestones.length; i++) {
			step = milestones[i];
			if (i > FDD_WALKTHROUGH) // miss the first workflow step, already have it
				pcd.add(Calendar.SECOND, (int) (step.getPctEffort() * estimate));

			fmd = new FeatureMilestone();
			fmd.setFddpmaMilestoneID(step.getWorkflowStep());
			fmd.setJiraWorkflowStepID(step.getJiraStep());
			fmd.setPlannedDate(pcd.getTime());
			list.add(fmd);
		}

		return list;
	}

	/**
	 * Update the FeatureMilestone array with actual dates.
	 * 
	 * @param issue
	 * @param list
	 * @return List<FeatureMilestone>
	 * @throws ConfigException
	 */
	public static List<FeatureMilestone> setActualDates(FeatureIssue issue, List<FeatureMilestone> list,
			WorkflowStep step) throws ConfigException {

		boolean pastCurrentStep = false;

		for (Iterator<FeatureMilestone> iterator = list.iterator(); iterator.hasNext();) {

			FeatureMilestone fmd = (FeatureMilestone) iterator.next();
			if (pastCurrentStep) {
				fmd.setActualDate(null);
			} else {
				if (fmd.getActualDate() == null)
					fmd.setActualDate(fmd.getPlannedDate());
			}

			if (step.getJiraStep() == getJiraStatusFromMilestone(fmd)) {
				pastCurrentStep = true;
				fmd.setActualDate(issue.getUpdated().getTime());
			}
		}
		return list;
	}

	public static String getJiraStatusFromMilestone(FeatureMilestone md) {
		for (int i = 0; i < milestones.length; i++) {
			if (md.getFddpmaMilestoneID() == milestones[i].getWorkflowStep()) {
				return milestones[i].getJiraStep();
			}
		}
		return null;
	}

	/**
	 * Returns the actual date and the milestone id
	 * 
	 * @param issue
	 * @return FeatureMilestone
	 * @throws ConfigException
	 */
	public static FeatureMilestone getActualDate(FeatureIssue issue) throws ConfigException {
		WorkflowStep step = getCurrentMilestoneStep(issue);
		FeatureMilestone milestoneDates = new FeatureMilestone();

		milestoneDates.setActualDate(issue.getUpdated().getTime());
		milestoneDates.setFddpmaMilestoneID(step.getWorkflowStep());
		return milestoneDates;
	}

}
