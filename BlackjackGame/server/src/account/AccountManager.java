package account;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import model.Account;
import model.ROLE;

public class AccountManager {
	private Map<String, Account> accounts = new HashMap<>();
	private boolean isLoaded = false;
	private static final String FILE_PATH = "src/resource/data.txt"; // File to store accounts
	private static AccountManager instance = null;	
	
	private AccountManager() {
		this.accounts = new HashMap<String, Account>();
		loadAccounts();
	}
	
	public static AccountManager getInstance() {
		if (instance == null) {
			instance = new AccountManager();
		}
		
		return instance;
	}

	public void loadAccounts() {
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					String username = parts[0];
					String password = parts[1];
					ROLE role;
					 try {
		                    role = ROLE.valueOf(parts[2].toUpperCase()); // Convert to Uppercase to match enum constants
		                } catch (IllegalArgumentException e) {
		                    System.err.println("Invalid role for user " + username + ": " + parts[2]);
		                    continue; // Skip this account if the role is invalid
		                }
					 String balance = parts[3];
					Account accountToAdd = new Account(username, password, role);
					accountToAdd.setBalance(Double.parseDouble(balance));
					accounts.put(username, accountToAdd);
				}
			}
		} catch (IOException e) {
			System.err.println("Error loading accounts: " + e.getMessage());
		}
		this.isLoaded = true;
	}


	public boolean login(String username, String password) {
		Account account = accounts.get(username);
		return account != null && account.getPassword().equals(password); // Check credentials
	}
	
	public double deposit(String username, double amount) {
		Account account = accounts.get(username);
		account.deposit(amount);
		
		saveAccounts();
		
		return account.getBalance();
		
	}
	
	private void saveAccounts() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
			for (Account account : accounts.values()) {
				writer.write(account.getUsername() + "," + account.getPassword() + "," + account.getRole() + "," + account.getBalance());
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error saving accounts: " + e.getMessage());
		}
	}
}

