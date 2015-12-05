package it.polito.dp2.WF.sol1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class ConcreteWorkflowMonitor implements it.polito.dp2.WF.WorkflowMonitor {
	
	private HashMap<String, ProcessReader> processes;
	private HashMap<String, WorkflowReader> workflows;

	//TODO: devo considerare che si possa creare un WorkflowManager vuoto?!
	public ConcreteWorkflowMonitor() {/*default constructor*/}

	//if I valid the document before creating the element I can assume some stuff
	public ConcreteWorkflowMonitor(Set<ProcessReader> processes, Set<WorkflowReader> workflows)
																	throws IllegalArgumentException {
		if( (processes == null) || (processes.size() == 0) )
    		throw new IllegalArgumentException("Wrong parameter, \"processes\" was null or empty!");
		
		if( (workflows == null) || (workflows.size() == 0) )
			throw new IllegalArgumentException("Wrong parameter, \"workflows\" was null or empty!");
		
		this.processes = new HashMap<String, ProcessReader>();
		int code = 1;	//TODO: maybe could be something wrong on this use of code...
		for(ProcessReader proc : processes) {
			this.processes.put("p"+code, proc);
			code++;
		}
		this.workflows = new HashMap<String, WorkflowReader>();
		for(WorkflowReader wfr : workflows) {
			this.workflows.put(wfr.getName(), wfr);
		}
	}
	
	public ConcreteWorkflowMonitor(Element element) throws IllegalArgumentException {
		if (element == null)
    		throw new IllegalArgumentException("Wrong parameter, element was null!");
    	
		NodeList wfNodes = element.getElementsByTagName("workflow");
		workflows = new HashMap<String, WorkflowReader>();
	    for (int i=0; i<wfNodes.getLength(); i++) {
	    	if(wfNodes.item(i) instanceof Element) {
	    		WorkflowReader wf = new ConcreteWorkflowReader((Element) wfNodes.item(i));
	    		workflows.put(wf.getName(), wf);
	    	}
	    }
		
		NodeList procNodes = element.getElementsByTagName("process");
		processes = new HashMap<String, ProcessReader>();
		int code = 1;
		for (int i=0; i<procNodes.getLength(); i++) {
	    	ProcessReader proc = new ConcreteProcessReader(wfNodes.item(i));
	    	processes.put("p"+code, proc);
	    	code++;
	    }
	
		return;
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return new TreeSet<ProcessReader>(processes.values());
	}

	@Override
	public WorkflowReader getWorkflow(String name) {
		return workflows.get(name);
	}

	@Override
	public Set<WorkflowReader> getWorkflows() {	//TODO: test this method
		return new TreeSet<WorkflowReader>(workflows.values());
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer("Inside this WorkflowMonitor there are:\n");
		
		if(workflows==null)
			buf.append("\tNo Workflows\n");
		else {
			buf.append("\tWorkflows:\n");
			for(WorkflowReader wfr : workflows.values())
				buf.append("\t\t"+wfr.toString()+"\n");
		}
		
		if(processes==null)
			buf.append("\tNo Processes\n");
		else {
			buf.append("\tProcesses:\n");
			for(ProcessReader pr : processes.values())
				buf.append("\t\t"+pr.toString()+"\n");
		}
		return buf.toString();
	}
	
	public void setParameter(Element element) {
		if (element == null)
    		throw new IllegalArgumentException("Wrong parameter, element was null!");
    	
		NodeList wfNodes = element.getElementsByTagName("workflow");
		workflows = new HashMap<String, WorkflowReader>();
	    for (int i=0; i<wfNodes.getLength(); i++) {
	    	if(wfNodes.item(i) instanceof Element) {
	    		WorkflowReader wf = new ConcreteWorkflowReader((Element) wfNodes.item(i));
	    		workflows.put(wf.getName(), wf);
	    	}
	    }
		
		NodeList procNodes = element.getElementsByTagName("process");
		processes = new HashMap<String, ProcessReader>();
		int code = 1;
		for (int i=0; i<procNodes.getLength(); i++) {
	    	ProcessReader proc = new ConcreteProcessReader(wfNodes.item(i));
	    	processes.put("p"+code, proc);
	    	code++;
	    }
	
		return;
	}

}
