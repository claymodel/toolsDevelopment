package com.pv.trjira;

public class WorkflowStep {

	private String jiraStep;
	private int workflowStep;
	private double pctEffort;
	private double pctTotalEffort;
	
	
	
	public WorkflowStep(String jiraStep, int workflowStep, double pctEffort, double pctTotalEffort) {
		super();
		this.jiraStep = jiraStep;
		this.pctEffort = pctEffort;
		this.pctTotalEffort = pctTotalEffort;
		this.workflowStep = workflowStep;
	}
	public String getJiraStep() {
		return jiraStep;
	}
	public void setJiraStep(String jiraStep) {
		this.jiraStep = jiraStep;
	}
	public int getWorkflowStep() {
		return workflowStep;
	}
	public void setWorkflowStep(int workflowStep) {
		this.workflowStep = workflowStep;
	}
	public double getPctEffort() {
		return pctEffort;
	}
	public void setPctEffort(double pctEffort) {
		this.pctEffort = pctEffort;
	}
	public double getPctTotalEffort() {
		return pctTotalEffort;
	}
	public void setPctTotalEffort(double pctTotalEffort) {
		this.pctTotalEffort = pctTotalEffort;
	}

	

}
