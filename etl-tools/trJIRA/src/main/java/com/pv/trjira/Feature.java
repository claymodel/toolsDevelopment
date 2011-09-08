package com.pv.trjira;

public class Feature {

	int featureId;
	int workpackageId;
	int activityId;
	int projectId;
	String featureName;
	String description;
	String reasonBehind;
	int sequence;
	int cancelled;
	int ownerUserId;
	String jiraId;
	int currentMilestone;

	public int getFeatureId() {
		return featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public int getWorkpackageId() {
		return workpackageId;
	}

	public void setWorkpackageId(int workpackageId) {
		this.workpackageId = workpackageId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReasonBehind() {
		return reasonBehind;
	}

	public void setReasonBehind(String reasonBehind) {
		this.reasonBehind = reasonBehind;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getCancelled() {
		return cancelled;
	}

	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}

	public int getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(int ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}

	public int getCurrentMilestone() {
		return currentMilestone;
	}

	public void setCurrentMilestone(int currentMilestone) {
		this.currentMilestone = currentMilestone;
	}

}
