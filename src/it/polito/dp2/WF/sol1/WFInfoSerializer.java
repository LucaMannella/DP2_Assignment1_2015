package it.polito.dp2.WF.sol1;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import it.polito.dp2.WF.util.DomParseV;
import it.polito.dp2.WF.util.DomUtil;
import it.polito.dp2.WF.util.WFAttributes;
import it.polito.dp2.WF.util.WFElements;

/**
 * This class serialize a Workflow into an XML file.
 * @author Luca (Sarcares)
 */
public class WFInfoSerializer {
	
	private WorkflowMonitor monitor;
	private DateFormat dateFormat;
	private Document doc;
	private Element root;
	
	public static final String XML_Declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String DTD_Declaration = "<!DOCTYPE WorkflowManager SYSTEM \"wfInfo.dtd\" >";
	public static final String STYLE_Declaration = "<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>";
	public static final String ROOT_Element = WFElements.WORKFLOW_MANAGER;

	/**
	 * Default constructor - Same as WFInfo
	 * @throws WorkflowMonitorException 
	 * @throws ParserConfigurationException 
	 */
	public WFInfoSerializer() throws WorkflowMonitorException, ParserConfigurationException {
		WorkflowMonitorFactory WFfactory = WorkflowMonitorFactory.newInstance();
		monitor = WFfactory.newWorkflowMonitor();
		
		doc = DomUtil.createEmptyDOMDocument(false);
		if(doc==null)
			throw new DOMException((short)13, "It's impossible to create a DOM!");
		
		// Create and append the root element
		root = (Element) doc.createElement(ROOT_Element);
		doc.appendChild(root);
		
		// This element will help to managing the data format
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");	//z for timezone and MM for millis
	}
	
	
	public static void main(String[] args) {
		// This class should receive the name of the output file.
		if(args.length != 1) {
			System.err.println("Error! Usage: <program_name> <output.xml>");
			System.err.println("args.length is equal to "+args.length);
			return;
		}
		System.out.println("This program will serialize your workflow into an XML file!");
		
		try { 
			WFInfoSerializer wf = new WFInfoSerializer();
			
			wf.appendWorkflows();
			wf.appendProcesses();
			wf.appendActors();
			
			wf.printDOM(System.out);
			PrintStream fpout = new PrintStream(new File(args[0]));
			wf.printDOM(fpout);

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
		} catch (IOException e) {
			System.err.println("It's impossible to create the XML file: "+e.getMessage());
			e.printStackTrace();
			System.exit(21);
		} catch (TransformerException e) {
			System.err.println("There is an error during the creation of the XML file: "+e.getMessage());
			e.printStackTrace();
			System.exit(22);
		}

		System.out.println("The created file will be validated");
		DomParseV.main(args);
		System.out.println("The parsing was completed!");
		return;
	}
	
	
	/**
	 * This method prints a DOM inside an XML file
	 * @param out - The name that you want to assign to the DOM document that will be written.
	 * @throws TransformerException 
	 */
	private void printDOM(PrintStream out) throws TransformerException {
		TransformerFactory xformFactory = TransformerFactory.newInstance();
		Transformer transformer = xformFactory.newTransformer();
		
		//set DTD declaration
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "wfInfo.dtd");
		//set indentation features
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		
		Source input = new DOMSource(this.doc);
		Result output = new StreamResult(out);
		
		transformer.transform (input, output);		//the XML file was created
		return;
	}
	//another way for setting the doctype
	//DocumentType docType = doc.getImplementation().createDocumentType("body", "SYSTEM", "wfInfo.dtd");
	//transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());

	/**
	 * This method appends the information related to the workflows to the DOM structure of the class.
	 */
	private void appendWorkflows() {
				
		// Get the list of workflows
		Set<WorkflowReader> WorkFlows = monitor.getWorkflows();
		// For each workflow create related data
		for (WorkflowReader wfr: WorkFlows)
		{
			String wfName = wfr.getName();
			//creating a workflow with the related name
			Element workflow = doc.createElement( WFElements.WORKFLOW );
			workflow.setAttribute(WFAttributes.WORKFLOW_NAME , wfName);
	
			// Get the list of actions
			Set<ActionReader> Actions = wfr.getActions();
			
			for (ActionReader ar: Actions)
			{
				String aName = ar.getName();						//taking action name
				String id = wfName+"_"+aName;						//building the ID
				String automInst;
				if( ar.isAutomaticallyInstantiated() )
					automInst = "true";
				else
					automInst = "false";
				//creating an action to append to the related workflow
				Element action = doc.createElement( WFElements.ACTION );
				action.setAttribute( WFAttributes.ACTION_NAME, aName );
				action.setAttribute( WFAttributes.ACTION_ID, id );
				action.setAttribute( WFAttributes.ACTION_ROLE, ar.getRole() );
				action.setAttribute( WFAttributes.ACTION_INSTANTIATION, automInst );
				
				Element subAction;
				if (ar instanceof SimpleActionReader) {
					//creating a simple_action
					subAction = doc.createElement( WFElements.SIMLE_ACTION );
					// taking next actions
					Set<ActionReader> setNext = ((SimpleActionReader)ar).getPossibleNextActions();
					
					if( !setNext.isEmpty() ) {
						StringBuffer attributes = new StringBuffer();
						for(ActionReader a : setNext) {
							attributes.append( wfName+"_"+a.getName()+" " );
						}
						
						subAction.setAttribute( WFAttributes.ACTION_SIMPLE_NEXT , attributes.toString().trim() );
					}
				}
				else if (ar instanceof ProcessActionReader) {
					//creating a process_action
					subAction = doc.createElement( WFElements.PROCESS_ACTION );
					subAction.setAttribute( WFAttributes.ACTION_PROCESS_NEXT, ((ProcessActionReader)ar).getActionWorkflow().getName() );
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
	 * This method appends the information related to the processes to the DOM structure of the class.
	 */
	private void appendProcesses() {
		int code = 1;
		
		// For each process print related data
		Set<ProcessReader> Processes = monitor.getProcesses();
		
		for (ProcessReader pr: Processes) {
			String startTime = dateFormat.format(pr.getStartTime().getTime());
			String wfName = pr.getWorkflow().getName();
			// creating a process
			Element process = doc.createElement( WFElements.PROCESS );
			// setting its attributes
			process.setAttribute( WFAttributes.PROCESS_CODE, "p"+code );
			process.setAttribute( WFAttributes.PROCESS_WFNAME, wfName );
			process.setAttribute( WFAttributes.PROCESS_TIME, startTime);
			
			// For each action print related data
			List<ActionStatusReader> statusSet = pr.getStatus();
			
			for (ActionStatusReader asr : statusSet) {
				Element action = doc.createElement( WFElements.ACTION_STATUS );
								
				action.setAttribute( WFAttributes.ACTION_STATUS_NAME, wfName+"_"+asr.getActionName() );
				
				if (asr.isTakenInCharge()) {		//was the action assigned?
					String actor = asr.getActor().getName().replaceAll(" ", "_");
					action.setAttribute( WFAttributes.ACTION_STATUS_ACTOR, actor );
					if (asr.isTerminated())	{		//was the action completed?
						String endTime = dateFormat.format( asr.getTerminationTime().getTime() );
						action.setAttribute( WFAttributes.ACTION_STATUS_TIME, endTime );
					}
					else {
						action.setAttribute( WFAttributes.ACTION_STATUS_TIME, "Not Finished" );
					}
				}
				else
					action.setAttribute( WFAttributes.ACTION_STATUS_TIME, "Not Taken" );
				
				process.appendChild(action);	//appending the action to the process
			}
			
			root.appendChild(process);			//appending the process to the root
			code++;
		}
		
		return;
	}

	/**
	 * This method appends the information related to the actors that develop the processes to the DOM structure of the class. 
	 */
	private void appendActors() {
		Set<Actor> actorsSet = new HashSet<Actor>();
		
		// creating the actors container
		Element actors = doc.createElement( WFElements.ACTORS );
		
		// Taking all the processes
		Set<ProcessReader> Processes = monitor.getProcesses();
		for (ProcessReader pr: Processes)
		{	
			// Taking all the action of a process
			List<ActionStatusReader> actionStatusSet = pr.getStatus();
			for (ActionStatusReader asr : actionStatusSet)
			{									// Taking the actor from each action
				if(asr.isTakenInCharge())		// only if the action was taken in charge
					actorsSet.add( asr.getActor() );
			}
		}
		
		Element actor;
		for (Actor a : actorsSet) {
			actor = doc.createElement( WFElements.ACTOR );
			actor.setAttribute( WFAttributes.ACTOR_NAME, a.getName().replaceAll(" ", "_") );
			actor.setAttribute( WFAttributes.ACTOR_ROLE, a.getRole() );
			actors.appendChild(actor);		//appending the actor to the set
		}
		
		root.appendChild(actors);			//appending the actors set to the root
		return;
	}
	
}
