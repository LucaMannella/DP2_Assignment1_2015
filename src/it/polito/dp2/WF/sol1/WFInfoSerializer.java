package it.polito.dp2.WF.sol1;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.FactoryConfigurationError;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.DomParseV;

/**
 * This class serialize a Workflow into an XML file.
 * @author Luca (Sarcares)
 */
public class WFInfoSerializer {
	
	private WorkflowMonitor monitor;
	private DateFormat dateFormat;
	private Document doc;
	
	public static final String XML_Declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String DTD_Declaration = "<!DOCTYPE WorkflowManager SYSTEM \"wfInfo.dtd\" >";
	public static final String STYLE_Declaration = "<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>";
	public static final String ROOT_Element = "WorkflowManager";

	/**
	 * Default constructor - Same as WFInfo
	 * @throws WorkflowMonitorException 
	 * @throws ParserConfigurationException 
	 */
	public WFInfoSerializer() throws WorkflowMonitorException, ParserConfigurationException {
		WorkflowMonitorFactory WFfactory = WorkflowMonitorFactory.newInstance();
		monitor = WFfactory.newWorkflowMonitor();
		
		doc = createDOMDocument();
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");	//z for timezone and MM for millis
	}
	
	
	public static void main(String[] args) {
		// This class should receive the name of the output file.
		if(args.length != 1) {
			System.err.println("Error! Usage: "+args[0]+" <output.xml>");
			System.err.println("args.length is equal to "+args.length);
			return;
		}
		System.out.println("This program will serialize your workflow into an XML file!");
		

		
		try { 
			WFInfoSerializer wf = new WFInfoSerializer();
			
			if(wf.doc==null)
				throw new DOMException((short)13, "It's impossible to create a DOM!");
			
			// Create and append the root element
			Element root = (Element) wf.doc.createElement(ROOT_Element);
			wf.doc.appendChild(root);
			
			wf.appendWorkflows(root);
			wf.appendProcesses(root);
			wf.appendActors(root);
			
			// Print the DOM into an XML file
//			fpout.println(XML_Declaration);
//			fpout.println(DTD_Declaration);
			wf.printDOM(wf.doc);


		} catch (WorkflowMonitorException e) {
			System.err.println("Could not instantiate the manager class: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (FactoryConfigurationError e) {
			System.err.println("Could not create a DocumentBuilderFactory: "+e.getMessage());
			e.printStackTrace();
			System.exit(11);
		} catch (ParserConfigurationException e) {
			System.err.println("Could not create a DocumentBuilder: "+e.getMessage());
			e.printStackTrace();
			System.exit(12);
		}
		
		System.out.println("The created file will be validated");
		DomParseV.main(args);
		System.out.println("The parsing was completed!");
		return;
	}
	
	
	/**
	 * This method prints a DOM inside an XML file
	 * @param doc - The DOM document that you want to write.
	 */
	private void printDOM(Document doc) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method appends the information related to the workflows to a DOM structure.
	 * @param root - The root of the DOM document that you want to write.
	 */
	private void appendWorkflows(Element root) {	//TODO: maybe is finished
				
		// Get the list of workflows
		Set<WorkflowReader> WorkFlows = monitor.getWorkflows();
		// For each workflow create related data
		for (WorkflowReader wfr: WorkFlows)
		{
			String wfName = wfr.getName();
			//creating a workflow with the related name
			Element workflow = doc.createElement("workflow");
			workflow.setAttribute("name", wfName);
	
			// Get the list of actions
			Set<ActionReader> Actions = wfr.getActions();
			
			for (ActionReader ar: Actions)
			{
				String aName = ar.getName();						//taking action name
				String id = wfName+"_"+aName;						//building the ID
				//creating an action to append to the related workflow
				Element action = doc.createElement("action");
				action.setAttribute("name", aName);
				action.setAttribute("id", id);
				action.setAttribute("role", ar.getRole());
				
				Element subAction;
				if (ar instanceof SimpleActionReader) {
					//creating a simple_action
					subAction = doc.createElement("simple_action");
					// taking next actions
					Set<ActionReader> setNext = ((SimpleActionReader)ar).getPossibleNextActions();
					
					Iterator<ActionReader> it = setNext.iterator();
					while(it.hasNext()) {
						String attributes;
						if( subAction.hasAttributes() ){
							attributes = subAction.getAttribute("nextActions");
							attributes += " "+wfName+"_"+it.next().getName();
						}
						else{
							attributes = wfName+"_"+it.next().getName();
						}
						subAction.setAttribute("nextActions", attributes);					
					}
				}
				else if (ar instanceof ProcessActionReader) {
					//creating a process_action
					subAction = doc.createElement("process_action");
					subAction.setAttribute( "nextProcess", ((ProcessActionReader)ar).getActionWorkflow().getName() );
				}
				else {
					//should be tested
					subAction = (Element) doc.createComment("Element NOT Recognized");
				}
				action.appendChild(subAction);	//appending the subaction to the action
				
				workflow.appendChild(action);	//appending action to the workflow
			}
			root.appendChild(workflow);			//appending workflow to the document
		}
		return;
	}

	/**
	 * This method appends the information related to the processes to a DOM structure.
	 * @param root - The root of the DOM document that you want to write.
	 */
	private void appendProcesses(Element root) {		//TODO: this method should be re-implemented
		int code = 1;
		
		// For each process print related data
		Set<ProcessReader> Processes = monitor.getProcesses();
		
		for (ProcessReader wfr: Processes) {
			String workflow = wfr.getWorkflow().getName();
			
			String fields = "code=\"p"+code+"\" "+
				"started=\""+dateFormat.format(wfr.getStartTime().getTime())+"\" "+
					"workflow=\""+workflow+"\"";
			
			doc.println("\t<process "+fields+">");			//opening process
			code++;
			// example <process code="p1" started="20/10/2015 08:30" workflow="ArticleProduction">	//
			
			// For each action print related data
			List<ActionStatusReader> statusSet = wfr.getStatus();
			
			for (ActionStatusReader asr : statusSet) {
				//<action_status action="NormalSale_GoodsDelivery" actor="Tom_Tomson" timestamp="20/10/15 11:23"/>
				fields = "action=\""+workflow+"_"+asr.getActionName()+"\" ";
				
				if (asr.isTakenInCharge()) {		//was the action assigned?
					fields += "actor=\""+asr.getActor().getName().replaceAll(" ", "_")+"\" ";
					if (asr.isTerminated())			//was the action completed?
						fields += "timestamp=\""+dateFormat.format(asr.getTerminationTime().getTime())+"\"";
					else
						fields += "timestamp=\"Not Finished\"";
				}
				else
					fields += "timestamp=\"Not Taken\"";
				
				doc.println("\t\t<action_status "+fields+"/>");		//printing the action details
			}
			
			doc.println("\t</process>");	//closing process
		}
		return;
	}

	/**
	 * This method appends the information related to the actors that develop the processes to a DOM structure.
	 * @param root - The root of the DOM document that you want to write.
	 */
	private void appendActors(Element root) {				//TODO: this method should be re-implemented
		Set<Actor> actors = new HashSet<Actor>();
		root.println("\t<actors>");
		
		// Taking all the processes
		Set<ProcessReader> Processes = monitor.getProcesses();
		for (ProcessReader wfr: Processes)
		{	
			// Taking all the action of a process
			List<ActionStatusReader> statusSet = wfr.getStatus();
			for (ActionStatusReader asr : statusSet)
			{	
				if(asr.isTakenInCharge()) {		//only if the action was taken in charge
					// Taking the actor from each action
					actors.add( asr.getActor() );
				}
			}
		}
		
		for (Actor a : actors) {
			String fields = "name=\""+a.getName().replaceAll(" ", "_")+"\" role=\""+a.getRole()+"\"";
			root.println("\t\t<actor "+fields+"/>");
			//example <actor name="John_Doe" role="Journalist"/>
		}
		
		root.println("\t</actors>");
		return;
	}
	
	/**  
	 * This method create a DOM Document object.
	 * @return	- The DOM Document
	 * @throws ParserConfigurationException 
	 */
	private Document createDOMDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}
}
