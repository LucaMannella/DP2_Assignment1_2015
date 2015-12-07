package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.util.WFAttributes;

public abstract class ConcreteActionReader implements ActionReader {

	private String name;
	private String role;
	private boolean automInst;
	private WorkflowReader parent;

	public ConcreteActionReader(Element action, WorkflowReader parent) {
		this.name = action.getAttribute( WFAttributes.ACTION_NAME.toString() );					//"name"
		this.role = action.getAttribute( WFAttributes.ACTION_ROLE.toString() );					//"role"
		
		String isAuto = action.getAttribute( WFAttributes.ACTION_INSTANTIATION.toString() );	//"automInst"
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
