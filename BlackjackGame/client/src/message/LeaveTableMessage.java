package message;

import abstractMessages.AbstractJoinTableMessage;
import state.StateManager;

public class LeaveTableMessage extends AbstractJoinTableMessage{

	private static final long serialVersionUID = 1L;

	public LeaveTableMessage() {
		this.setUsername(StateManager.getInstance().getAccount().getUsername());
	}
	
	public LeaveTableMessage(String tableId) {
		this.setUsername(StateManager.getInstance().getAccount().getUsername());
		this.setTableId(tableId);
	}
	
	@Override
	public Object execute() {
		// No Execute on client side
		return null;
	}

}
