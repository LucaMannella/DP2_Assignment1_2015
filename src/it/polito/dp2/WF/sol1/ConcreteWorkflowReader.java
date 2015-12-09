package it.polito.dp2.WF.sol1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.util.WFAttributes;
import it.polito.dp2.WF.util.WFElements;

/**
 * This is a concrete implementation of the interface WorkflowReader.<BR><BR>
 * If you want more detail about the interface look to {@link it.polito.dp2.WF.WorkflowReader}
 * 
 * @author Luca
 */
public class ConcreteWorkflowReader implements WorkflowReader, Comparable<WorkflowReader> {

	private String name;
	private Map<String, ActionReader> actions;
	private Set<ProcessReader> processes;

	public ConcreteWorkflowReader(Element workflow, NodeList procNodes) {
		actions = new HashMap<String, ActionReader>();
		processes = new HashSet<ProcessReader>();
		
//TODO:	if(workflow == null) return;	//safety lock
		this.name = workflow.getAttribute( WFAttributes.WORKFLOW_NAME );				//"name"
		
		// set the actions inside the object
		NodeList actionNodes = workflow.getElementsByTagName( WFElements.ACTION );		//"action"
		for (int i=0; i<actionNodes.getLength(); i++) {
			if(actionNodes.item(i) instanceof Element) {		//if it's not an element I ignore it
				Element azione = (Element) actionNodes.item(i);
				ActionReader ar;
				
				if(! actions.containsKey( azione.getAttribute(WFAttributes.ACTION_NAME) )) {
					//I create a new action only if they does not still exists
					if( azione.getElementsByTagName(WFElements.PROCESS_ACTION).getLength() >= 1 )	//"process_action"
						ar = new ProcessAction(azione, this);
					else
						ar = new SimpleAction(azione, this);				
					
		    		actions.put(ar.getName(), ar);
				}
	    	}
		}
		
//TODO:	if(procNodes == null) return;		//safety lock 
		// set the processes that refer this Workflow
		for(int i=0; i<procNodes.getLength(); i++) {
			if(procNodes.item(i) instanceof Element) {
				Element e = (Element) procNodes.item(i);
				if( e.getAttribute(WFAttributes.PROCESS_WFNAME).equals(this.name) ) {
					//if(process.workflowName == workflow.name) 
					ProcessReader p = new ConcreteProcessReader(e, this);
					processes.add(p);
				}
			}
		}
		
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<ActionReader> getActions() {
		return new TreeSet<ActionReader>(actions.values());
	}

	@Override
	public ActionReader getAction(String actionName) {
		return actions.get(actionName);
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return processes;
	}

	@Override
	public int compareTo(WorkflowReader o) {
		return this.name.compareTo(o.getName());
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer("Workflow: "+name+"\n");
		
		buf.append("Actions:\n");
		for(ActionReader ar : actions.values()) {
			buf.append("\t"+ar.toString()+"\n");
		}
		buf.append("Processes:\n");
		for(ProcessReader pr : processes) {
			buf.append("\t"+pr.toString()+"\n");
		}
		
		return buf.toString();
	}

	public void addAction(ActionReader ar) {
		actions.put(ar.getName(), ar);
	}
	
}
