package it.polito.dp2.WF.sol1;

import org.w3c.dom.Element;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol1.util.WFAttributes;

/**
 * This is an abstract implementation of the interface ActionReader based on JAXP framework.<BR>
 * If you want to use that class you have to instantiate one of the following implementation.<BR>
 * {@link it.polito.dp2.WF.sol1.SimpleAction}<BR>{@link it.polito.dp2.WF.sol1.ProcessAction}<BR><BR>
 * If you want more detail about the interface look to {@link it.polito.dp2.WF.ActionReader}
 * 
 * @author Luca
 */
public abstract class AbstractActionReader implements ActionReader {

	private String name;
	private String role;
	private boolean automInst;
	private WorkflowReader parent;

	public AbstractActionReader(Element action, WorkflowReader parent) {
//TODO:	if(action == null)	return;
		this.name = action.getAttribute( WFAttributes.ACTION_NAME );				//"name"
		this.role = action.getAttribute( WFAttributes.ACTION_ROLE );				//"role"
		
		String isAuto = action.getAttribute( WFAttributes.ACTION_INSTANTIATION );	//"automInst"
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
	
	@Override
	public String toString() {
		return "\tAction: "+name+" - Requested Role: "+role+" - Parent workflow: "+parent.getName()+" - AutomInst: "+automInst;
	}

}
