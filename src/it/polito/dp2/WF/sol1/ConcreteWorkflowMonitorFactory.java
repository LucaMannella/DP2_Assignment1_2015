package it.polito.dp2.WF.sol1;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;

/**
 * @author Luca
 */
public class ConcreteWorkflowMonitorFactory extends it.polito.dp2.WF.WorkflowMonitorFactory {

    public ConcreteWorkflowMonitorFactory() { }

	/**
	 * This method creates an instance of my concrete class that implements the WorkflowMonitor interface.
	 */
	@Override
	public WorkflowMonitor newWorkflowMonitor() throws WorkflowMonitorException {
		WorkflowMonitor myMonitor = new ConcreteWorkflowMonitor();
		return myMonitor;
	}

}
