import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.pv.trjira.ConfigException;
import com.pv.trjira.FeatureIssue;
import com.pv.trjira.FeatureMilestone;
import com.pv.trjira.JFDDSync;

public class TestDates {

	@Before
	public void setUp() throws Exception {
	}

	/*
	 * Test new jira item halfway through workflow. feature id | milestone id | step | planned | actual 14 | 2 | 10001|
	 * 01/01/09| 02/01/09
	 */
	@Test
	public final void testReturnPlannedActualDates() throws ConfigException {
		FeatureIssue issue = new FeatureIssue();
		FeatureMilestone fmd = new FeatureMilestone();
		issue.setStatus(JFDDSync.JIRA_STATUS_CODE_UNIT_TEST); // workflow step
		Calendar updated = Calendar.getInstance();
		issue.setUpdated(updated); // today

//		List<FeatureMilestone> list = JFDDSync.setPlannedDates(issue, new Long(8640000));
//
//		for (int i = 0; i < list.size(); i++) {
//			fmd = (FeatureMilestone) list.get(i);
//			System.out.println(fmd.getFddpmaMilestoneID() + ":" + fmd.getJiraWorkflowStepID() + ":"
//					+ fmd.getPlannedDate());
//
//		}
	}

	@Test
	public final void testMilestoneSteps() throws ConfigException {
		FeatureIssue issue = new FeatureIssue();
		issue.setStatus(JFDDSync.JIRA_STATUS_DESIGN_INSPECTION);
//		WorkflowStep step = JFDDSync.getCurrentMilestoneStep(issue);
//		System.out.println("JIRA_STATUS_DESIGN: " + step.getWorkflowStep());
//
//		assert (step.getWorkflowStep() == JFDDSync.milestones[2].getWorkflowStep());
	}

	@Test
	public final void testSetActualDates() throws ConfigException {
		FeatureIssue issue = new FeatureIssue();
		FeatureMilestone fmd = new FeatureMilestone();

		issue.setStatus(JFDDSync.JIRA_STATUS_CODE_INSPECTION); // workflow step
//		WorkflowStep step = JFDDSync.getCurrentMilestoneStep(issue);
//
//		// date we updated JIRA item to push it onto the next stage.
//		Calendar updated = Calendar.getInstance();
//		issue.setUpdated(updated); // today
//
//		int estimateDays = 10;
//
//		List<FeatureMilestone> milestoneDates = JFDDSync.setPlannedDates(issue, new Long(86400) * estimateDays);
//
//		milestoneDates = JFDDSync.setActualDates(issue, milestoneDates, step);
//
//		for (int i = 0; i < milestoneDates.size(); i++) {
//			fmd = (FeatureMilestone) milestoneDates.get(i);
//			System.out.println(fmd.getFddpmaMilestoneID() + ":" + fmd.getJiraWorkflowStepID() + "\t Planned:"
//					+ fmd.getPlannedDate() + " Actual: " + fmd.getActualDate());
//
//		}

	}
}
