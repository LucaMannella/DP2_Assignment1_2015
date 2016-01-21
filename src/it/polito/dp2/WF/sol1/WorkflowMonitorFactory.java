package it.polito.dp2.WF.sol1;

 import org.xml.sax.SAXParseException;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;

/**
 * This is a concrete implementation of the interface {@link it.polito.dp2.WF.WorkflowMonitorFactory}.
 * 
 * @author Luca
 */
public class WorkflowMonitorFactory extends it.polito.dp2.WF.WorkflowMonitorFactory {

	/**
	 * This method creates an instance of the {@link ConcreteWorkflowMonitor} class, 
	 * a concrete implementation of the {@link WorkflowMonitor} interface.
	 */
	@Override
	public WorkflowMonitor newWorkflowMonitor() throws WorkflowMonitorException {
		try {
			WorkflowMonitor myMonitor = new ConcreteWorkflowMonitor();
			System.out.println("WorkflowMonitor created!");
			return myMonitor;
		} 
		catch (SAXParseException e) {
			System.out.println("** Parsing Error"
		            + ", file " + e.getSystemId()
		            + ", line " + e.getLineNumber());
			System.out.println("   " + e.getMessage() );
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	//toString() implemented for debugging purposes
	@Override
	public String toString(){
		return "This is a custom WorkflowMonitorFactory implementation for the assignment 1.";
	}

}
