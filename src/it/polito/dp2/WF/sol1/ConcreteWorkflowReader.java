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
		// set the workflow's name
		this.name = workflow.getAttribute("name");
		
		// set the actions inside the object
		NodeList actionNodes = workflow.getElementsByTagName("action");
		actions = new HashMap<String, ActionReader>();
		for (int i=0; i<actionNodes.getLength(); i++) {
			if(actionNodes.item(i) instanceof Element) {
				Element azione = (Element) actionNodes.item(i);
				ActionReader ar;
				
				if(name.equals("dodo")) //TODO: if azione.getFiglio().getName().equals("simple_action")
					ar = new SimpleAction(azione, this);
				else
					ar = new ProcessAction(azione, this);
				
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
		
		return "Workflow: "+name+" - poi ci metterò le azioni e i processi";
	}

	
}
