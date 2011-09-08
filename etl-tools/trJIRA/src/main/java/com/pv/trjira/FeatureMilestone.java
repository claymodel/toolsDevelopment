package com.pv.trjira;

import java.util.Date;

public class FeatureMilestone {

	private String jiraWorkflowStepID;
	private int fddpmaMilestoneID;
	private Date plannedDate;
	private Date actualDate;
	private int featureId;

	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	/**
	 * @param fddpmaMilestoneID the fddpmaMilestoneID to set
	 */
	public void setFddpmaMilestoneID(int fddpmaMilestoneID) {
		this.fddpmaMilestoneID = fddpmaMilestoneID;
	}

	/**
	 * @return the fddpmaMilestoneID
	 */
	public int getFddpmaMilestoneID() {
		return fddpmaMilestoneID;
	}

	/**
	 * @param jiraWorkflowStepID the jiraWorkflowStepID to set
	 */
	public void setJiraWorkflowStepID(String jiraWorkflowStepID) {
		this.jiraWorkflowStepID = jiraWorkflowStepID;
	}

	/**
	 * @return the jiraWorkflowStepID
	 */
	public String getJiraWorkflowStepID() {
		return jiraWorkflowStepID;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public int getFeatureId() {
		return featureId;
	}

}
