package it.polito.dp2.WF.sol1.util;

/**
 * This class contains all the attributes that can appear inside the XML file described
 * inside {@link <i>root</i>/dtd/wfInfo.dtd}
 * 
 * @author Luca
 */
public class WFAttributes
{	
	public final static String WORKFLOW_NAME = "name";
	
	public final static String ACTION_ID = "id";
	public final static String ACTION_NAME = "name";
	public final static String ACTION_ROLE = "role";
	public final static String ACTION_INSTANTIATION = "automInst";
	public final static String ACTION_SIMPLE_NEXT = "nextActions";
	public final static String ACTION_PROCESS_NEXT = "nextProcess";
	
	public final static String PROCESS_CODE = "code";
	public final static String PROCESS_TIME = "started";
	public final static String PROCESS_WFNAME = "workflow";
	
	public final static String ACTION_STATUS_NAME = "action";
	public final static String ACTION_STATUS_ACTOR = "actor";
	public final static String ACTION_STATUS_TIME = "timestamp";
		public final static String STATUS_NOT_FINISHED = "Not Finished";
		public final static String STATUS_NOT_TAKEN = "Not Taken";
	
	public final static String ACTOR_NAME = "name";
	public final static String ACTOR_ROLE = "role";	
}
