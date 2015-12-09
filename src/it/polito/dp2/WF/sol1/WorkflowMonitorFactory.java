package it.polito.dp2.WF.sol1;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;

/**
 * This is a concrete implementation of the interface WorkflowMonitorFactory.<BR><BR>
 * If you want more detail about the interface look to {@link it.polito.dp2.WF.WorkflowMonitorFactory}
 * 
 * @author Luca
 */
public class WorkflowMonitorFactory extends it.polito.dp2.WF.WorkflowMonitorFactory {

    public WorkflowMonitorFactory() { }

	/**
	 * This method creates an instance of my concrete class that implements the WorkflowMonitor interface.
	 */
	@Override
	public WorkflowMonitor newWorkflowMonitor() throws WorkflowMonitorException {
		WorkflowMonitor myMonitor = new ConcreteWorkflowMonitor();
		System.out.println("WorkflowMonitor created!");
		return myMonitor;
	}
	
	public String toString(){
		return "This is a custom WorkflowMonitorFactory implementation.";
	}

}
