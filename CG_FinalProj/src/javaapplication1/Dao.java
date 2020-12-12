/*
 * Name: Cheryl Gardner 
 * Date: 12/10/2020
 * Program: Dao.java
 * ITMD 411 Final Project
 * Purpose: Define all of the necessary programs including creating the necessary tables, making a connection
 * adding users, and inserting, selecting, view, delete, and update records using a ticket id
 */

//Import javaapplication1 package
package javaapplication1;

//Import all of the necessary packages for the program
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

//Create Dao class for connections and record purposes
public class Dao {
	// Declare the necessary instances fields
	static Connection connect = null;
	Statement statement = null;

	// Create the Dao Constructor
	public Dao() {
	  
	}

	//Define a new method called connection that will create the SQL connection
	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			//Try to connect to the JDBC sql papademas using the user fp411 and password 411
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
			//Tell the user that the connection was made
			System.out.println("Connection was made");
		} 
		//If the connection cannot be made, then tell the user that the connection failed
		catch (SQLException e) {
			e.printStackTrace();
		}
		//Return the connection to the Dao program
		return connect;
	}

	//Declare the Creation of Tables method to create the necessary tables
	public void createTables() {
		// Create the tickets table to hold the ticket records and the users table to hold all of the valid users for login
		final String createTicketsTable = "CREATE TABLE cgard_tickets1(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), start_date VARCHAR(50), status VARCHAR(15))";
		final String createUsersTable = "CREATE TABLE cgard_users1(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		//Try this block to see if it works
		try {
			//Create a new statement and update to add the necessary tables for creation
			statement = getConnection().createStatement();
			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			//Tell the user that the tables were created successfully
			System.out.println("Tables were successfully created in the database");
			
			//Close the statement and the connection
			statement.close();
			connect.close();
		} 
		//Throw an error statement if it does not work
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		//Call the add users method
		addUsers();
	}

	//Create a new method called add users
	public void addUsers() {

		//Declare all of the variables that are needed for this method
		String sql;
		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		//Try to read the user.csv file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			//Declare a new String variable called line and split all of the tables for the username, password and admin id
			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} 
		//If the try block does not work, tell the user the file cannot be read
		catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		//Create a new try block to insert all of the data from user.csv into the user table
		try {
			// Create a new statement based on the connection
			statement = getConnection().createStatement();

			//As long as the rowData is in the array, keep using the for loop
			for (List<String> rowData : array) {
				//Create a new sql statement to add the username, password and admin for the three values
				sql = "insert into cgard_users1(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				//Execute the sql statement to update
				statement.executeUpdate(sql);
			}
			//Tell the user that the insert was successfully
			System.out.println("The users were successfully inserted into the database");

			//Close the statement object
			statement.close();
		} 
		//Throw an exception if the sql statement does not work
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//Declare a new method to insert the Records into the table
	public int insertRecords(String ticketName, String ticketDesc) {
		//Declare a new variable called id
		int id = 0;
		//Create a new Prepared statement to add all of the records into the ticket system 
		try {
			PreparedStatement insert = connect.prepareStatement("INSERT into cgard_tickets1 (ticket_issuer, ticket_description, start_date, status) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			String startTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
			insert.setString(1, ticketName);
			insert.setString(2, ticketDesc);
			insert.setString(3, startTime);
			insert.setString(4, "OPEN");
			insert.executeUpdate();

			//Retrieve the ticket id from the insertion statement
			ResultSet resultSet = null;
			resultSet = insert.getGeneratedKeys();
			if (resultSet.next()) {
				//Retrieve the first field in the table
				id = resultSet.getInt(1);
			}
		} 
		//Throw an exception statement if it does not work
		catch (SQLException e) {
			e.printStackTrace();
		}
		//Return the id variable to the insertion
		return id;

	}

	public ResultSet refreshTable() {
		ResultSet results = null;
		try {
			PreparedStatement refresh = connect.prepareStatement("SELECT * FROM cgard_tickets1");
			results = refresh.executeQuery();
		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	
	//Create the readRecords Method to view the records
	public ResultSet readRecords() {
		//Declare the resultSet as empty
		ResultSet results = null;
		//Create a try block with the Prepared Statement to select all of the data from the ticket table
		try {
			PreparedStatement read = connect.prepareStatement("SELECT * FROM cgard_tickets1");
			results = read.executeQuery();
			//read.close();
		} 
		//If the statement does not work then throw an SQL Exception
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		//Return the result set back to the method
		return results;
	}
	
	//Create a new method called SelectRecords for a user to be able to see their specific ticket
	public ResultSet selectRecords(int tid) {
		//Declare resultSet as empty
		ResultSet results = null;
		//Create a new try block to select all of the fields of a specific row using the user's ticket ID
		try {
			PreparedStatement select = connect.prepareStatement("SELECT * FROM cgard_tickets1 WHERE ticket_id = ?");
			select.setLong(1, tid);
			results = select.executeQuery();
			//select.close();
		}
		//If it does not work, throw the error back to the user
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		//Return the result set back to the method
		return results;
	}

	//Declare a new method called updateRecords
	public void updateRecords(int tid) {
		//Create a new try block to update the Records using SQL Prepared Statements
		try {
			//Declare two variables called newDesc and newStatus to hold the status and description for the update
			String newDesc = null;
			String newStatus = null;
			//Ask the user if they want to update the description and the status 
			int answer1 = JOptionPane.showConfirmDialog(null, "Do you want to update the ticket description of Ticket ID: " + tid + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
			int answer2 = JOptionPane.showConfirmDialog(null, "Do you want to close Ticket ID: " + tid + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
			//If the user says they want to update both fields to this
			if (answer1 == JOptionPane.YES_OPTION && answer2 == JOptionPane.YES_OPTION) {
				//Ask the user what they want the new description and set the status to be closed
				newDesc = JOptionPane.showInputDialog(null, " What would you like the new description of Ticket ID: " + tid + " to be?");
				newStatus = "CLOSED";
				//Create a new prepared statement that updates the description, clears start time and closes ticket using a specific ticket id
				PreparedStatement update = connect.prepareStatement("UPDATE cgard_tickets1 SET ticket_description = ?, start_date = ?, status = ? WHERE ticket_id = ?");
				update.setString(1,newDesc);
				update.setString(2, "");
				update.setString(3, newStatus);
				update.setLong(4, tid);
				update.executeUpdate();
				update.close();
				//Tell the user the ticket was updated and closed successfully
				System.out.println("Ticket ID : " + tid + " was updated successfully");
				JOptionPane.showMessageDialog(null, "Ticket ID  " + tid + " was successfully updated");
				System.out.println("Ticket id: " + tid + " was successfully closed");
				JOptionPane.showMessageDialog(null, "Ticket id: " + tid + " was closed successfully");
			}
			//If the user only wants to update the description do this
			else if (answer1 == JOptionPane.YES_OPTION && answer2 == JOptionPane.NO_OPTION) {
				//Ask the user what the new description is
				newDesc = JOptionPane.showInputDialog(null, " What would you like the new description to be?");
				//Create a new prepared statement that updates the description using a certain ticket id
				PreparedStatement update = connect.prepareStatement("UPDATE cgard_tickets1 SET ticket_description = ? WHERE ticket_id = ?");
				update.setString(1, newDesc);
				update.setLong(2, tid);
				update.executeUpdate();
				update.close();
				//Tell the user that the ticket was updated successfully
				System.out.println("Ticket ID : " + tid + " was updated successfully");
				JOptionPane.showMessageDialog(null, "Ticket ID  " + tid + " was successfully updated");
			}
			//If the user only wants to update the status do this
			else if (answer1 == JOptionPane.NO_OPTION && answer2 == JOptionPane.YES_OPTION) {
				//Set the new status to be closed
				newStatus = "CLOSED";
				//Create a new prepared statement that clears the start date and updates the status to be closed
				PreparedStatement update = connect.prepareStatement("UPDATE cgard_tickets1 SET start_date = ?, status = ? WHERE ticket_id = ?");
				update.setString(1, "");
				update.setString(2,newStatus);
				update.setLong(3, tid);
				update.executeUpdate();
				update.close();
				//Tell the user that the ticket was closed and updated successfully
				System.out.println("Ticket ID : " + tid + " was updated successfully");
				JOptionPane.showMessageDialog(null, "Ticket ID  " + tid + " was successfully updated");
				System.out.println("Ticket id: " + tid + " was successfully closed");
				JOptionPane.showMessageDialog(null, "Ticket id: " + tid + " was closed successfully");
			}
			//If the user does not want to update either do this
			else if (answer1 == JOptionPane.NO_OPTION && answer2 == JOptionPane.NO_OPTION) {
				//Tell the user that the update fails
				JOptionPane.showMessageDialog(null, "The following ticket could not be updated");
				System.out.println("Update failed");
			}
		}
		//If the try block does not work, throw an exception
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//Create a new method called deleteRecords to delete a record using a specific ticket id
	public int deleteRecords(int tid) {
		//Create a new try block
		try {
			//Create a new Prepared statement that deletes the record using the given ticket id
			PreparedStatement delete = connect.prepareStatement("DELETE FROM cgard_tickets1 WHERE ticket_id = ?");
			delete.setLong(1, tid);
			delete.executeUpdate();
			delete.close();
		}
		//If it does not work then throw the exception
		catch (SQLException e) {
			e.printStackTrace();
		}
		//Return the ticket id to the method
		return tid;
	}
}
