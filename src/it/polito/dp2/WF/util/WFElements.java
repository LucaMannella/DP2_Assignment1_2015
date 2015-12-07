package it.polito.dp2.WF.util;

public enum WFElements {
	WorkflowManager,
	workflow,
		action,
			simple_action,
			process_action,
	process,
		action_status,
	actors,
		actor;
	
	//Possibile ridefinirlo per variare la stampa, non varia il metodo name();
	//public String toString(){	}
}
