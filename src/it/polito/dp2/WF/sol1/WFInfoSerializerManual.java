package it.polito.dp2.WF.sol1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.DomParseValidator;

/**
 * This class serialize a Workflow into an XML file.
 * @author Luca
 */
public class WFInfoSerializerManual {
	
	private WorkflowMonitor monitor;
	private DateFormat dateFormat;
	
	public static final String XML_Declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String DTD_Declaration = "<!DOCTYPE WorkflowManager SYSTEM \"wfInfo.dtd\" >";
	public static final String STYLE_Declaration = "<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>";
	public static final String ROOT_Element = "WorkflowManager";

	/**
	 * Default constructor - Same as WFInfo
	 * @throws WorkflowMonitorException 
	 */
	public WFInfoSerializerManual() throws WorkflowMonitorException {
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		monitor = factory.newWorkflowMonitor();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss z");	//z for timezone
	}
	
	/**
	 * Same constructor of class WFInfo
	 * @param monitor - A given WorkflowMonitor.
	 */
	public WFInfoSerializerManual(WorkflowMonitor monitor) {
		super();
		this.monitor = monitor;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss z");	//z for timezone
	}
	
	public static void main(String[] args) {
		// This class should receive the name of the output file.
		if(args.length != 1) {
			System.err.println("Error! Usage: "+args[0]+" <output.xml>");
			System.err.println("args.length is equal to "+args.length);
			return;
		}
		System.out.println("This program will serialize your workflow into an XML file!");
		
		WFInfoSerializerManual wf;
		try {
			wf = new WFInfoSerializerManual();
			
			PrintWriter fpout = new PrintWriter(new FileWriter(args[0]));
			fpout.println(XML_Declaration);
			fpout.println(DTD_Declaration);
			fpout.println("<"+ROOT_Element+">");
			wf.printWorkflows(fpout);
			fpout.println();
			wf.printProcesses(fpout);
			fpout.println();
			wf.printActors(fpout);
			fpout.println("</"+ROOT_Element+">");
			fpout.close();

		} catch (WorkflowMonitorException e) {
			System.err.println("Could not instantiate data generator: "+e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Could not create the output file: "+e.getMessage());
			e.printStackTrace();
			System.exit(2);
		}
		
		System.out.println("The created file will be validated");
		DomParseValidator.main(args);
		System.out.println("The parsing was completed!");
		return;
	}
	
	/**
	 * This method prints the information related to the workflows inside an XML file.
	 * @param fpout - The XML file that you want to write.
	 */
	private void printWorkflows(PrintWriter fpout) {
				
		// Get the list of workflows
		Set<WorkflowReader> WorkFlows = monitor.getWorkflows();
		
		// For each workflow print related data
		for (WorkflowReader wfr: WorkFlows) {
			String wfrName = wfr.getName();							//taking workflow name
			fpout.println("\t<workflow name=\""+wfrName+"\">");		//opening workflow
	
			// Get the list of actions
			Set<ActionReader> Actions = wfr.getActions();
			
			for (ActionReader ar: Actions) {
				String aName = ar.getName();						//taking action name
				String id = wfrName+"_"+aName;						//building the ID
				String fields = "id=\""+id+"\" name=\""+aName+"\" role=\""+ar.getRole()+"\"";
				
				fpout.println("\t\t<action "+fields+">");	//opening action
				if (ar instanceof SimpleActionReader) {
					
					// taking next actions
					Set<ActionReader> setNext = ((SimpleActionReader)ar).getPossibleNextActions();
					if(setNext.isEmpty())
						fpout.println("\t\t\t<simple_action />");
					else {
						Iterator<ActionReader> it = setNext.iterator();
						fields = wfrName+"_"+it.next().getName();	//I must have an element here.
						while(it.hasNext()) {
							fields += " "+wfrName+"_"+it.next().getName();
						}
						//printing the simple action
						fpout.println("\t\t\t<simple_action nextActions=\""+fields+"\" />");
					}
				}
				else if (ar instanceof ProcessActionReader) {
					// print workflow
					String nextWorkflow = ((ProcessActionReader)ar).getActionWorkflow().getName();
					fpout.println("\t\t\t<process_action nextProcess=\""+nextWorkflow+"\" />");
				}
				else {
					fpout.println("\t\t\t<!-- Element NOT Recognized -->");
				}
				fpout.println("\t\t</action>");	//closing action
			}
			fpout.println("\t</workflow>");	//closing workflow
		}
		return;
	}

	/**
	 * This method prints the information related to the processes inside an XML file.
	 * @param fpout - The XML file that you want to write.
	 */
	private void printProcesses(PrintWriter fpout) {
		int code = 1;
		
		// For each process print related data
		Set<ProcessReader> Processes = monitor.getProcesses();
		
		for (ProcessReader wfr: Processes) {
			String workflow = wfr.getWorkflow().getName();
			
			String fields = "code=\"p"+code+"\" "+
				"started=\""+dateFormat.format(wfr.getStartTime().getTime())+"\" "+
					"workflow=\""+workflow+"\"";
			
			fpout.println("\t<process "+fields+">");			//opening process
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
				
				fpout.println("\t\t<action_status "+fields+"/>");		//printing the action details
			}
			
			fpout.println("\t</process>");	//closing process
		}
		return;
	}

	/**
	 * This method prints the information related to the actors that develop the processes inside an XML file.
	 * @param fpout - The XML file that you want to write.
	 */
	private void printActors(PrintWriter fpout) {
		Set<Actor> actors = new HashSet<Actor>();
		fpout.println("\t<actors>");
		
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
			fpout.println("\t\t<actor "+fields+"/>");
			//example <actor name="John_Doe" role="Journalist"/>
		}
		
		fpout.println("\t</actors>");
		return;
	}

}
