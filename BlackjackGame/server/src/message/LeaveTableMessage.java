package message;

import abstractMessages.AbstractJoinTableMessage;
import account.AccountManager;
import model.Player;
import table.TableManager;

public class LeaveTableMessage extends AbstractJoinTableMessage{
	private static final long serialVersionUID = 1L;

	public LeaveTableMessage() {
		
	}

	@Override
	public Object execute() {
		Player player = AccountManager.getInstance().getPlayer(getUsername());
		
		if(player == null) {
			return null;
		}
		
		return TableManager.getInstance().leaveTable(getTableId(), player);
	}

}
