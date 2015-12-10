package it.polito.dp2.WF.sol1.util;

/**
 * This class contains all the elements that can appear inside the XML file described
 * inside {@link <i>root</i>/dtd/wfInfo.dtd}
 * 
 * @author Luca
 */
public class WFElements
{
	public final static String WORKFLOW_MANAGER = "WorkflowManager";
	
	public final static String WORKFLOW = "workflow";
		public final static String ACTION = "action";
			public final static String SIMPLE_ACTION = "simple_action";
			public final static String PROCESS_ACTION = "process_action";
			
	public final static String PROCESS = "process";
		public final static String ACTION_STATUS = "action_status";
		
	public final static String ACTORS = "actors";
		public final static String ACTOR = "actor";
}
