package it.polito.dp2.WF.sol1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;

public class ConcreteActionStatusReader implements ActionStatusReader {
	
	private SimpleDateFormat dateFormat;
	
	private String name;
	private Actor actor;
	private Calendar endTime;
	private boolean takenInCharge;
	private boolean terminated;

	public ConcreteActionStatusReader(Element action, String wfName) {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:MM z");
		endTime = Calendar.getInstance();
		
		String name = action.getAttribute("action");
		this.name = name.replace(wfName+"_", "");
		
		String timestamp = action.getAttribute("timestamp");
		String actorName = action.getAttribute("actor");
		
		if(actorName.equals("")) {
			actor = null;
			terminated = false;
			takenInCharge = false;
			endTime.setTimeInMillis(0);
		}
		else {
			//TODO: come faccio arrivare l'attore?!
			//this.actor = attore;
			takenInCharge = true;
			
			if(timestamp.equals("Not Finished")) {
				terminated = false;
				endTime.setTimeInMillis(0);
			}
			else {
				try {	//everything all right
					endTime.setTime(dateFormat.parse(timestamp));
					terminated = true;
				}
				catch (ParseException e) {	//something wrong happens
					terminated = false;
					takenInCharge = true;
					endTime.setTimeInMillis(0);
					System.err.println("Error parsing the action, this action is considered as Not Finished");
				}
			}
		}
		
	}

	@Override
	public String getActionName() {
		return this.name;
	}

	@Override
	public Actor getActor() {
		return actor;
	}

	@Override
	public Calendar getTerminationTime() {
		return endTime;
	}

	@Override
	public boolean isTakenInCharge() {
		return takenInCharge;
	}

	@Override
	public boolean isTerminated() {
		return terminated;
	}

}
