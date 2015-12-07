package it.polito.dp2.WF.sol1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class ConcreteWorkflowReader implements WorkflowReader, Comparable<WorkflowReader> {

	private String name;
	private Map<String, ActionReader> actions;
	private Set<ProcessReader> processes;

	public ConcreteWorkflowReader(Element workflow) {
		this.name = workflow.getAttribute("name");
		
		actions = new HashMap<String, ActionReader>();
		ActionReader ar;
		// set the actions inside the object
		NodeList actionNodes = workflow.getElementsByTagName("action");
		
		for (int i=0; i<actionNodes.getLength(); i++) {
			if(actionNodes.item(i) instanceof Element) {
				Element azione = (Element) actionNodes.item(i);
				
				if( azione.getElementsByTagName("process_action").getLength() >= 1 )
					ar = new ProcessAction(azione, this);
				else
					ar = new SimpleAction(azione, this);				
				
	    		actions.put(ar.getName(), ar);
	    	}
		}
		
		// TODO Gestire la faccenda relativa ai processi...
		processes = new TreeSet<ProcessReader>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<ActionReader> getActions() {
		return new TreeSet<>(actions.values());
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

	
}
