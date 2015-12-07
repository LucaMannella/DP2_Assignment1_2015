package it.polito.dp2.WF.myTests;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.ConcreteWorkflowMonitor;
import it.polito.dp2.WF.sol1.ConcreteWorkflowReader;
import it.polito.dp2.WF.util.DomUtil;
import it.polito.dp2.WF.util.WFAttributes;
import it.polito.dp2.WF.util.WFAttributesEnum;

/**
 * This class will be used for some testing in the first assignment.
 * @author Luca
 */
public class Prova {

	public static void main(String[] args) {
//		provaEnumKeywords();
		provaDOM();
//		provaRemoveWFName();
//		provaActorsInsideASR();
//		provaImportCalendar();
//		provaAbstractFactoryPattern();
//		provaStringSte();
//		prova01();
		return;
	}
	

	public static void provaEnumKeywords() {
		System.out.println("Prova di assegnazione");
		WFAttributesEnum e = WFAttributesEnum.valueOf("name");
		System.out.println("Ho generato l'elemento: "+e);
//		e = WFElements.valueOf("WorkFlowManager");				//genera una IllegalArgumentException
//		System.out.println("Ho generato un errore: "+e);		//unreachable code
		
		System.out.println("\n\nStampo il contenuto dell'Enum WFAttributes!");
		int i=1;
		for( WFAttributesEnum e2 : WFAttributesEnum.values() ) {
			System.out.println("Keyword "+i+": "+e2);				//equivalente a toString();
			System.out.println("getValue method: "+e2.getValue());
			System.out.println("name method: "+e2.name()+"\n");		//stampa esattamente il valore della keyword
			i++;
		}
	}


	public static void provaDOM() {
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
			if(!(monitor instanceof ConcreteWorkflowMonitor)) {
				System.err.println("Wrong instantiation, I can't proceed!");
				System.exit(-1);
			}
			else
				System.out.println("I have the right WorkflowMonitor!");
				
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
		    
		    for(ProcessReader pr : wfMonitor.getProcesses()) {
		    	for(ActionStatusReader asr : pr.getStatus()) {
		    		asr.toString();
		    	}
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


	public static void provaRemoveWFName() {
		String id = "Article_Production_Writing_News";
		String wfName = "Article_Production";
		
		String name = id.replace(wfName+"_", "");
		System.out.println("Il nome dell'azione è: "+name);
		return;
	}


	public static void provaActorsInsideASR() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
		Calendar startTime = Calendar.getInstance();
		
		Document doc = DomUtil.parseDomDocument("dtd/output.xml", true);
		if(doc == null) {
			System.err.println("Errore!");
			System.exit(-1);
		}
		
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		System.out.println("Ho creato la factory: "+factory.toString());
	
		try {
			WorkflowMonitor monitor = factory.newWorkflowMonitor();
						
			if(!(monitor instanceof ConcreteWorkflowMonitor)) {
				System.err.println("Wrong instantiation, I can't proceed!");
				System.exit(-1);
			}
				
			NodeList lista = doc.getElementsByTagName("WorkflowManager");
			Element root = (Element)lista.item(0);
						
			NodeList processes = root.getElementsByTagName("process");
			for(int i=0; i<processes.getLength(); i++) {
				Element proc = (Element) processes.item(i);
				
				NodeList actions = proc.getElementsByTagName("action_status");
				for(int j=0; j< actions.getLength(); j++) {
					Element act = (Element) actions.item(j);
					
					if( act.getAttribute("actor")==null )
						System.out.println(proc.getAttribute("workflow")
								+" - "+act.getAttribute("action")
								+" - Qui c'è un attore NULL!!!");
					else if( act.getAttribute("actor").equals("") )
						System.out.println(proc.getAttribute("workflow")
								+" - "+act.getAttribute("action")
								+" - Qui c'è un attore vuoto!!!");
					else
						System.out.println(act.getAttribute("actor"));
				}
				try {
					startTime.setTime( dateFormat.parse(proc.getAttribute("started")) );
				} catch (ParseException e) {
					System.err.println("Error parsing data, current time will be used");
					startTime.setTime( new Date() );
				}
				System.out.println("Fine for interno "+i);
			}
			System.out.println("- Fine for esterno -");
		} catch (WorkflowMonitorException e1) {
			System.err.println("No workflows");
			e1.printStackTrace();
		}
		System.out.println("Fine metodo");
	}


	public static void provaImportCalendar() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
		Calendar startTime = Calendar.getInstance();
		
		Document doc = DomUtil.parseDomDocument("dtd/output.xml", true);
		if(doc == null) {
			System.err.println("Errore!");
			System.exit(-1);
		}
		
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		System.out.println("Ho creato la factory: "+factory.toString());
	
		try {
			WorkflowMonitor monitor = factory.newWorkflowMonitor();
			
			
			if(!(monitor instanceof ConcreteWorkflowMonitor)) {
				System.err.println("Wrong instantiation, I can't proceed!");
				System.exit(-1);
			}
				
			NodeList lista = doc.getElementsByTagName("WorkflowManager");
			Element root = (Element)lista.item(0);
			NodeList workflows = root.getElementsByTagName("workflow");
			
			NodeList processes = root.getElementsByTagName("process");
			for(int i=0; i<processes.getLength(); i++) {
				Element proc = (Element) processes.item(i);
				
				try {
					startTime.setTime( dateFormat.parse(proc.getAttribute("started")) );
				} catch (ParseException e) {
					System.err.println("Error parsing data, current time will be used");
					startTime.setTime( new Date() );
				}
				System.out.println(dateFormat.format(startTime.getTime()));
			}
			System.out.println("Fine for");
		} catch (WorkflowMonitorException e1) {
			System.err.println("No workflows");
			e1.printStackTrace();
		}
		System.out.println("Fine metodo");
	}


	public static void provaAbstractFactoryPattern() {
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
	
	public static void provaStringSte() {
		String banana= new String();
		String ciao = "Ciao";
		banana+=ciao;
		System.err.println(banana+"\n");
	}
	
	public static void prova01() {
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
