package it.polito.dp2.WF.sol1;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class ConcreteWorkflowMonitor implements it.polito.dp2.WF.WorkflowMonitor {

	private HashMap<String, ProcessReader> processes;
	private HashMap<String, WorkflowReader> workflows;
	private HashMap<String, Actor> actors;

	//TODO: devo considerare che si possa creare un WorkflowManager vuoto?!
	public ConcreteWorkflowMonitor() {/*default constructor*/
		processes = new HashMap<String, ProcessReader>();
		workflows = new HashMap<String, WorkflowReader>();
	}
	//TODO: nell'implementazione di Sisto viene tutto generato nel costruttore di default 
	
	//if I valid the document before creating the element I can assume some stuff
	public ConcreteWorkflowMonitor(Set<ProcessReader> processes, Set<WorkflowReader> workflows, Set<Actor> actors)
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
		this.actors = new HashMap<String, Actor>();
		for(Actor a : actors) {
			this.actors.put(a.getName(), a);
		}
	}
	
	public ConcreteWorkflowMonitor(Element element) throws IllegalArgumentException {
		setParameter(element);
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
	
	public Actor getActor(String name) {
		return actors.get(name);
	}
	public Set<Actor> getActors() {
		return new TreeSet<Actor>(actors.values());
	}
	public void setParameter(Element root) {
		int i=0;
		if (root == null)
    		throw new IllegalArgumentException("Wrong parameter, element was null!");
    	
		NodeList wfNodes = root.getElementsByTagName("workflow");
		workflows = new HashMap<String, WorkflowReader>();
	    for (i=0; i<wfNodes.getLength(); i++) {
	    	if(wfNodes.item(i) instanceof Element) {
	    		WorkflowReader wf = new ConcreteWorkflowReader((Element) wfNodes.item(i));
	    		workflows.put(wf.getName(), wf);
	    	}
	    }
		
		NodeList procNodes = root.getElementsByTagName("process");
		processes = new HashMap<String, ProcessReader>();
		int code = 1;
		for (i=0; i<procNodes.getLength(); i++) {
			Element e = (Element) wfNodes.item(i);	//ottengo processo i-esimo
	    	ProcessReader proc = new ConcreteProcessReader( e, workflows.get(e.getAttribute("workflow")) );
	    	//I should have already the workflow because I read it before and the document should be valid
	    	processes.put("p"+code, proc);
	    	code++;
	    }
		
		//TODO: update this part if you want to manage more departments
		actors = new HashMap<String, Actor>();
		
		NodeList actorsNodes = root.getElementsByTagName("actors");
		// this loop is executed just one time in this particular application
		for(i=0; i<actorsNodes.getLength(); i++) {
			
			Element e = (Element) actorsNodes.item(i);
			NodeList acts = e.getElementsByTagName("actor");
			for(int j=0; j<acts.getLength(); j++) {
				e = (Element) acts.item(j);
				Actor a = new Actor( e.getAttribute("name"), e.getAttribute("role") );
				actors.put(a.getName(), a);
			}
		}
		
		return;
	}

}
