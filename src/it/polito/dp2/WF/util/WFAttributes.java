package it.polito.dp2.WF.util;

public enum WFAttributes {
	
	WORKFLOW_NAME("name"),
	
	ACTION_ID("id"),
	ACTION_NAME("name"),
	ACTION_ROLE("role"),
	ACTION_INSTANTIATION("automInst"),
	ACTION_SIMPLE_NEXT("nextActions"),
	ACTION_PROCESS_NEXT("nextProcess"),
	
	PROCESS_CODE("code"),
	PROCESS_TIME("started"),
	PROCESS_WFNAME("workflow"),
	ACTION_STATUS_NAME("action"),
	ACTION_STATUS_ACTOR("actor"),
	ACTION_STATUS_TIME("timestamp"),
	
	ACTOR_NAME("name"),
	ACTOR_ROLE("role");	
	
	private String value;	
	
	private WFAttributes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString(){
		return this.value;
	}
}
