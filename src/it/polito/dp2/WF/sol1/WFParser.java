package it.polito.dp2.WF.sol1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.util.DomParseErrorHandler;

public class WFParser {
	
	private WorkflowMonitor monitor;
	private DocumentBuilder builder;
	private DateFormat dateFormat;

	public WFParser(String inputFileName) 
			throws WorkflowMonitorException, ParserConfigurationException, SAXException, IOException {
		
		WorkflowMonitorFactory WFfactory = WorkflowMonitorFactory.newInstance();
		monitor = WFfactory.newWorkflowMonitor();
		
		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		builder.setErrorHandler( new DomParseErrorHandler() );
		builder.parse(inputFileName);
		
		// This element will help to managing the data format
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");	//z for timezone and MM for millis
	}
	
	
	public static void main(String[] args) {
		if((args.length != 1) || (args[0]==null)) {
			System.err.println("Error! Usage: <program_name> <output.xml>");
			System.err.println("args.length is equal to "+args.length);
			return;
		}
		
		try {
			WFParser p = new WFParser(args[0]);
		}
		catch (WorkflowMonitorException e) {
			System.err.println("Could not instantiate the manager class: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException ioe) {
			// I/O error
			System.err.println("Unable to open/read the file");
			System.exit(11);
		}
		catch (SAXParseException spe) {	
			// Parsing error
			System.err.println("\n** Parsing error"+", line "+spe.getLineNumber()+", uri "+spe.getSystemId());
			System.err.println("\t"+spe.getMessage());
			System.exit(21);
		}
		catch (SAXException se) {
			System.err.println("\n** Internal error.");
			System.err.println("\t"+se.getMessage());
			System.exit(22);
		}
		catch (ParserConfigurationException pce) {
			System.err.println("\n** Internal error.");
			System.err.println("\t Parser with specified options can't be built");
			System.exit(23);
		} 
	}

}
