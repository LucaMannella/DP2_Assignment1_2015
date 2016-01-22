package it.polito.dp2.WF.sol1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.WFElements;

/**
 * This is a concrete implementation of the interface {@link ProcessReader} based on the JAXP framework.
 * 
 * @author Luca
 */
public class ConcreteProcessReader implements ProcessReader, Comparable<ProcessReader> {

	private SimpleDateFormat dateFormat;
	
	private Calendar startTime;
	private WorkflowReader workflow;
	private List<ActionStatusReader> actionStatus;
	
	/**
	 * This method create an implementation of the {@link ProcessReader} interface.
	 * @param proc - The {@link Process} starting object.
	 * @param workflow - The {@link WorkflowReader} whom this process belongs.
	 * @throws SAXException If an actor is not able to perform an action.
	 */
	public ConcreteProcessReader(Element proc, WorkflowReader workflow) throws SAXException {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SS z");
		//dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
		startTime = Calendar.getInstance();
		
		this.workflow = workflow;
		
		if(proc == null) return;	//safety lock
		
		/* Calendar startTime creation */
		try {
			String s = proc.getAttribute("started");
			Date d = dateFormat.parse(s);
			startTime.setTime(d);
		}
		catch (ParseException e) {
			System.err.println("Error parsing that process, current time will be used");
			startTime.setTime( new Date() );
		}
		
		/* ActionStatusReader list creation */
		NodeList actionNodes = proc.getElementsByTagName( WFElements.ACTION_STATUS );	//"action_status"
		actionStatus = new ArrayList<ActionStatusReader>();
		for (int i=0; i<actionNodes.getLength(); i++) {
	    	ActionStatusReader asr = new ConcreteActionStatusReader( (Element) actionNodes.item(i), workflow.getName() );
	    	actionStatus.add(asr);
	    }
		
		return;
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
		buf.append("Started at: "+dateFormat.format(startTime.getTimeInMillis())+"\n");
		
		for(ActionStatusReader asr : actionStatus) {
			buf.append(asr.toString()+"\n");
		}
		return buf.toString();
	}
	
	/**
	 * This method gives a short version of the toString method.
	 * 
	 * @return A shorted version of the toStrin method.
	 */
	public String toShortString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		
		StringBuffer buf = new StringBuffer("Process started at ");
		buf.append( dateFormat.format(startTime.getTimeInMillis()) );
		buf.append(" with "+actionStatus.size()+" ActionStatus.");
		
		return buf.toString();
	}

}
