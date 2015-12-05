package it.polito.dp2.WF.sol1;

import java.util.Calendar;
import java.util.List;

import org.w3c.dom.Node;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

public class ConcreteProcessReader implements ProcessReader, Comparable<ProcessReader> {

	public ConcreteProcessReader(Node item) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Calendar getStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionStatusReader> getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowReader getWorkflow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(ProcessReader o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
