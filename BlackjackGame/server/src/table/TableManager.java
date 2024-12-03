package table;

import java.util.ArrayList;
import java.util.List;

import message.BroadcastLobbyTabblesMessage;
import model.Dealer;
import model.Lobby;
import model.LobbyTable;
import model.Player;
import serverModel.Table;
import model.AbstractTable;

public class TableManager {
	private ArrayList<AbstractTable> tables;
	private static TableManager instance = null;

	private TableManager() {
		this.tables = new ArrayList<>();
	}

	public static TableManager getInstance() {
		if (instance == null) {
			instance = new TableManager();
		}
		return instance;
	}

	//Method that return table Id
	public String createTable(Dealer dealer) {
		AbstractTable table = new Table(dealer);
		
		tables.add(table);
		
		// Update the lobby
		Lobby.getInstance().addNewTable(new LobbyTable(table.getId(), table.getNumPlayers(), table.getMinBet()));
		System.out.println("New Table with Lobby");
		System.out.println(Lobby.getInstance().getTableList());
		Lobby.getInstance().broadcast(new BroadcastLobbyTabblesMessage(Lobby.getInstance().getTableList()));
		
		return table.getId();
	}
	
	// Player Joins the lobby
	public void playerJoinsLobby(Player player) {
		Lobby.getInstance().addPlayerToLobby(player);
		Lobby.getInstance().broadcast(new BroadcastLobbyTabblesMessage(Lobby.getInstance().getTableList()));
	}
	
	public void dealerJoinsLobby(Dealer dealer) {
		Lobby.getInstance().addDealerToLobby(dealer);
		Lobby.getInstance().broadcast(new BroadcastLobbyTabblesMessage(Lobby.getInstance().getTableList()));
	}

	// list of table info
	public String toString() {
		StringBuilder tableList = new StringBuilder("Tables: \n");
		for (AbstractTable table : tables) {
			tableList.append("Table ID: ").append(table.getId()).append("Players: ").append(table.getNumPlayers())
					.append(",Open: ").append(table.isOpen()).append("\n");

		}
		return tableList.toString();
	}
	
	public List<AbstractTable> getTables(){
		return this.tables;
	}
	
	public List<LobbyTable> getLobbyTables() {
		return Lobby.getInstance().getTableList();
	}

	// return all active tables(open and not full)
	public ArrayList<AbstractTable> activeTables() {
		ArrayList<AbstractTable> active = new ArrayList<>();
		for (AbstractTable table : tables) {
			if (table.isOpen()) {
				active.add(table);
			}

		}
		return active;

	}

	// Add a player to a specific table
	// TODO: Make a table info class to give client table information
	public String joinTable(String tableId, Player player) {
		for (AbstractTable table : tables) {
			if (table.getId().equals(tableId)) {
				if (table.isOpen()) {
					table.joinTable(player); //method from Table class
					return table.getId();
				} else {
	                System.out.println("Table is closed or full.");
	            }
				break;
			}
		}
		return null; // Table full or not open
	}

	// Remove a player from a specific table
	public boolean leaveTable(String tableId, Player player) {
		for (AbstractTable table : tables) {
			if (table.getId().equals(tableId) && table.getPlayerList().contains(player)) {
				table.leaveTable(player);
				return true;
			}
			break;

		}
		return false; // Table not found
	}
	
	public AbstractTable getTableById(String id) {
		for (AbstractTable table : tables) {
			if (table.getId().equals(id)) {
				return table;
			}
		}
		return null;
	}

	public Lobby getLobby() {
		return Lobby.getInstance();
	}
}
