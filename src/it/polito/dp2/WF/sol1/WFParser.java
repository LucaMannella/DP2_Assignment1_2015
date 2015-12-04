package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;

public class WFParser {
	
	private WorkflowMonitor monitor;
	private DocumentBuilder builder;
	private DateFormat dateFormat;

	public WFParser() throws WorkflowMonitorException, ParserConfigurationException {
		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		WorkflowMonitorFactory WFfactory = WorkflowMonitorFactory.newInstance();
		monitor = WFfactory.newWorkflowMonitor();
		
		// This element will help to managing the data format
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");	//z for timezone and MM for millis
	}
	
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Error! Usage: <program_name> <output.xml>");
			System.err.println("args.length is equal to "+args.length);
			return;
		}
		
		//WFParser p = new WFParser();
		//p.builder.parse(args[0]);

	}

}
