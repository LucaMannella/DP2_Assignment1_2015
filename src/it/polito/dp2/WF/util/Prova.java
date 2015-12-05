package it.polito.dp2.WF.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.ConcreteWorkflowMonitor;
import it.polito.dp2.WF.sol1.ConcreteWorkflowReader;

/**
 * This class will be used for some testing in the first assignment.
 * @author Luca
 */
public class Prova {

	public static void main(String[] args) {
		provaDOM();
		
		return;
	}
	

	private static void provaAbstractFactoryPattern() {
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		System.out.println("Ho creato la factory: "+factory.toString());
		WorkflowMonitor monitor;
		try {
			monitor = factory.newWorkflowMonitor();
			System.out.println("Ho creato il mio monitor: "+monitor.toString());
		} 
		catch (WorkflowMonitorException e) {
			System.err.println("Non sono riuscito a creare il workflow monitor: "+e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void provaDOM() {
		// This element will help to managing the data format
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
		
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		System.out.println("Ho creato la factory: "+factory.toString());
				
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setValidating(true);
		System.out.println("Ho creato la document factory");
	
		WorkflowMonitor monitor;
		DocumentBuilder builder;
		Document document;
		try {
			builder = documentFactory.newDocumentBuilder();
			document = builder.parse("dtd/output.xml");
			System.out.println("validation completed");
			
			monitor = factory.newWorkflowMonitor();
			NodeList lista = document.getElementsByTagName("WorkflowManager");
			Element root = (Element)lista.item(0);
			((ConcreteWorkflowMonitor) monitor).setParameter(root);
			System.out.println("Ho creato il mio monitor:\n"+monitor.toString());
			
			
			System.out.println("C'è "+lista.getLength()+" elemento chiamato WorkflowManager");
			
			NodeList wfNodes = root.getElementsByTagName("workflow");
			System.out.println("Ci sono "+wfNodes.getLength()+" elementi chiamati workflow");
			
			NodeList wfProc = root.getElementsByTagName("process");
			System.out.println("Ci sono "+wfProc.getLength()+" elementi chiamati process");
			
			WorkflowMonitor wfMonitor = new ConcreteWorkflowMonitor(root);
			System.out.println("Nel mio oggetto ci sono: "+wfMonitor.getWorkflows().size()+" workflows");
			System.out.println("Nel mio oggetto ci sono: "+wfMonitor.getProcesses().size()+" processi");
			
			Map<String, WorkflowReader>workflows = new HashMap<String, WorkflowReader>();
		    for (int i=0; i<wfNodes.getLength(); i++) {
		    	if(wfNodes.item(i) instanceof Element) {
		    		WorkflowReader wf = new ConcreteWorkflowReader((Element) wfNodes.item(i));
		    		workflows.put(wf.getName(), wf);
		    		System.out.println("Elemento "+i+" aggiunto!");
		    	}
		    	else
		    		System.err.println("Banana!!!");
		    }
			
		} catch (WorkflowMonitorException e) {
			System.err.println("Non sono riuscito a creare il workflow monitor: "+e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		} catch (ParserConfigurationException e) {
			System.err.println("Error creating the DocumentBuilder!");
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
		
		
	}

	private static void provaStringSte() {
		String banana= new String();
		String ciao = "Ciao";
		banana+=ciao;
		System.err.println(banana+"\n");
	}
	
	private static void prova01() {
		String wfrName = "MyWorkFlow1";
		String aName = "MyAction1";
		String role = "MyRole1";
		String id = wfrName+"_"+aName;

		String fields = "id=\""+id+"\" name=\""+aName+"\" role=\""+role+"\"";
		System.out.println("The builded String is: \n\t"+fields+"\n");
		
		System.out.println("<WorkflowManager>");
		System.out.println("\t<workflow name=\""+wfrName+"\">");
		System.out.println("\t\t<action "+fields+" />");
		System.out.println("\t</workflow>");
		System.out.println("</WorkflowManager>");
	}

}
