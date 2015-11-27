package it.polito.dp2.WF.sol1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowMonitorFactory;
import it.polito.dp2.WF.WorkflowReader;

/**
 * This class serialize a Workflow into an XML file.
 * @author Luca (Sarcares)
 */
public class WFInfoSerializer {
	
	private WorkflowMonitor monitor;
	private DateFormat dateFormat;
	
	public static final String XML_Declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String DTD_Declaration = "<!DOCTYPE WorkflowManager SYSTEM \"wfInfo.dtd\" >";
	public static final String STYLE_Declaration = "<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>";
	public static final String ROOT_Element = "WorkflowManager";

	/**
	 * Default constructror - Same as WFInfo
	 * @throws WorkflowMonitorException 
	 */
	public WFInfoSerializer() throws WorkflowMonitorException {
		WorkflowMonitorFactory factory = WorkflowMonitorFactory.newInstance();
		monitor = factory.newWorkflowMonitor();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	}
	
	/**
	 * Same constructor of class WFInfo
	 * @param monitor - A given WorkflowMonitor.
	 */
	public WFInfoSerializer(WorkflowMonitor monitor) {
		super();
		this.monitor = monitor;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	}
	
	public static void main(String[] args) {
		// This class should receive the name of the output file.
		if(args.length != 1) {
			System.err.println("Error! Usage: "+args[0]+" <output.xml>");
			System.err.println("args.length is equal to "+args.length);
			return;
		}
		System.out.println("This program will serialize your workflow into an XML file!");
		
		WFInfoSerializer wf;
		try {
			wf = new WFInfoSerializer();
			
			PrintWriter fpout = new PrintWriter(new FileWriter(args[0]));
			fpout.println(XML_Declaration);
			fpout.println(DTD_Declaration);
			fpout.println("<"+ROOT_Element+">");
			wf.printWorkflows(fpout);
			//wf.printProcesses(fpout);
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

	//TODO: method copied by WFInfo. please, modify it!
	private void printProcesses(PrintWriter fpout) {

		// Get the list of processes
		Set<ProcessReader> WorkFlows = monitor.getProcesses();
		
		/* Print the header of the table */
		fpout.println("#");
		fpout.println("#Number of Processes: "+WorkFlows.size());
		fpout.println("#");
		//-String header = new String("#List of processes:");
		//-printHeader(header);
		
		// For each process print related data
		for (ProcessReader wfr: WorkFlows) {
			fpout.println("Process started at " + 
								dateFormat.format(wfr.getStartTime().getTime()) +
					            " "+"- Workflow " + wfr.getWorkflow().getName());
			fpout.println("Status:");
			List<ActionStatusReader> statusSet = wfr.getStatus();
			//-printHeader("Action Name\tTaken in charge by\tTerminated");
			for (ActionStatusReader asr : statusSet) {
				fpout.print(asr.getActionName()+"\t");
				if (asr.isTakenInCharge())
					fpout.print(asr.getActor().getName()+"\t\t");
				else
					fpout.print("-"+"\t\t\t");
				if (asr.isTerminated())
					fpout.println(dateFormat.format(asr.getTerminationTime().getTime()));
				else
					fpout.println("-");
			}
			fpout.println("#");
		}
		fpout.println("#End of Processes");
		fpout.println("#");
	}

}
