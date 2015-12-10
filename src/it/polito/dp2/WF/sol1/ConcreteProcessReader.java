package it.polito.dp2.WF.sol1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.WFElements;

/**
 * This is a concrete implementation of the interface ProcessReader.<BR><BR>
 * If you want more detail about the interface look to {@link it.polito.dp2.WF.ProcessReader}
 * 
 * @author Luca
 */
public class ConcreteProcessReader implements ProcessReader, Comparable<ProcessReader> {

	private SimpleDateFormat dateFormat;
	
	private Calendar startTime;
	private WorkflowReader workflow;
	private List<ActionStatusReader> actionStatus;
	
	public ConcreteProcessReader(Element proc, WorkflowReader workflow) {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
		startTime = Calendar.getInstance();
		
//TODO:	if(workflow == null) return;	//safety lock
		this.workflow = workflow;
		
//TODO:	if(proc == null) return;	//safety lock
		/* Calendar startTime creation */
		try {
			String s = proc.getAttribute("started");
//			System.out.println("DEBUG - la stringa recuperata è: "+s.toString());
			Date d = dateFormat.parse(s);
//			System.out.println("DEBUG - la data è parsificata è: "+d.toString());
			startTime.setTime(d);
//			System.out.println("DEBUG - se stampo il calendario ottengo: "+startTime.toString());
		} catch (ParseException e) {
			System.err.println("Error parsing that process, current time will be used");
			startTime.setTime( new Date() );
		}
		
		/* ActionStatus list creation */
		NodeList actionNodes = proc.getElementsByTagName( WFElements.ACTION_STATUS );	//"action_status"
		actionStatus = new ArrayList<ActionStatusReader>();
		for (int i=0; i<actionNodes.getLength(); i++) {
	    	ActionStatusReader asr = new ConcreteActionStatusReader( (Element) actionNodes.item(i), workflow.getName() );
	    	actionStatus.add(asr);
	    }
	}

	@Override
	public Calendar getStartTime() {
		return this.startTime;
	}

	@Override
	public List<ActionStatusReader> getStatus() {
		return actionStatus;
	}

	@Override
	public WorkflowReader getWorkflow() {
		return this.workflow;
	}

	/**
	 * The comparison is based on the starting time. 
	 * If the time are equal is based on the name of the relative workflows.
	 */
	@Override
	public int compareTo(ProcessReader o) {
		int toRet = startTime.compareTo(o.getStartTime());
		if(toRet == 0)
			toRet = workflow.getName().compareTo(o.getWorkflow().getName());
		
		return toRet;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Process related to workflow: "+workflow.getName()+" ");
		buf.append("Started at: "+dateFormat.format(startTime.getTime())+"\n");
		
		for(ActionStatusReader asr : actionStatus) {
			buf.append(asr.toString()+"\n");
		}
		return buf.toString();
	}

}
