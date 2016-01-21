package it.polito.dp2.WF.myTests;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;

public class WFParser {
	
	final static String filename="dtd/wfInfo.xml";

	public static void main(String[] args) {
		
		//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SS z");
		
		//set system property
		System.setProperty("it.polito.dp2.WF.sol1.WFInfo.file", filename);
		System.setProperty("it.polito.dp2.WF.WorkflowMonitorFactory", "it.polito.dp2.WF.sol1.WorkflowMonitorFactory");
		
		WorkflowMonitor monitor = null;
		try {
			monitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();
		}
		catch (WorkflowMonitorException e) {
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println(monitor);
	}

}
