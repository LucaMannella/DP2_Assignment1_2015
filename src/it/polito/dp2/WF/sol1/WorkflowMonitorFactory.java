package it.polito.dp2.WF.sol1;

 import org.xml.sax.SAXException;
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
		String exitCode;
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
			exitCode = "1";
			throw new WorkflowMonitorException(e, exitCode);
		} catch (SAXException e) {
			System.out.println("Invalid Document! \n"
					+ e.getMessage());
			e.printStackTrace();
			exitCode = "2";
			throw new WorkflowMonitorException(e, exitCode);
		}
	}
	
	//toString() implemented for debugging purposes
	@Override
	public String toString(){
		return "This is a custom WorkflowMonitorFactory implementation for the assignment 1.";
	}

}
