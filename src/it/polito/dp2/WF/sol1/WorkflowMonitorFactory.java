package it.polito.dp2.WF.sol1;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;

/**
 * @author Luca
 */
public class WorkflowMonitorFactory extends it.polito.dp2.WF.WorkflowMonitorFactory {

	private static WorkflowMonitorFactory myInstance = null;

	private WorkflowMonitorFactory() {
		// TODO Probably I don't need to fill the constructor
	}
	
	public static WorkflowMonitorFactory newInstance() {
		if(myInstance == null)
			myInstance = new WorkflowMonitorFactory();
		
		return myInstance;
	}

	/**
	 * This method creates an instance of my concrete class that implements the WorkflowMonitor interface.
	 */
	@Override
	public WorkflowMonitor newWorkflowMonitor() throws WorkflowMonitorException {
		// TODO Auto-generated method stub
		WorkflowMonitor myMonitor = new MyWorkflowMonitor();
		return myMonitor;
	}

}
