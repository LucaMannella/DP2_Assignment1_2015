package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.WorkflowReader;

public abstract class ConcreteActionReader implements ActionReader {

	private String name;
	private String role;
	private boolean automInst;
	private WorkflowReader parent;

	public ConcreteActionReader(Element action, WorkflowReader parent) {
		this.name = action.getAttribute("name");
		this.role = action.getAttribute("role");
		
		String isAuto = action.getAttribute("automInst");
		if( isAuto.equalsIgnoreCase("true") )
			this.automInst = true;
		else
			this.automInst = false;
		
		this.parent = parent;
	}

	@Override
	public WorkflowReader getEnclosingWorkflow() {
		return this.parent;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getRole() {
		return this.role;
	}

	@Override
	public boolean isAutomaticallyInstantiated() {
		return automInst;
	}

}
