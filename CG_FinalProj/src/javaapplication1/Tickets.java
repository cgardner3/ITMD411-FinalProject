/*
 * Name: Cheryl Gardner 
 * Date: 12/10/2020
 * Program: Tickets.java
 * ITMD 411 Final Project
 * Purpose: Create the gui ticket menus for both admins and users as well as add listeners for each of the buttons 
 * when they are pressed by a user or admin
 */

//Import the javaapplication1 package for the program
package javaapplication1;

//Import all of the necessary packages that are needed for the program
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
//Declare a new public class called tickets that extends JFrame and implements Action Listener
public class Tickets extends JFrame implements ActionListener {

	//Declare and initialize the class variables
	Dao dao = new Dao(); 
	Boolean chkIfAdmin = null;

	//Create the three menus, File, Admin and Tickets
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	//Create all of the menu items for the three menus
	JMenuItem mnuItemExit;
	JMenuItem mnuItemRefresh;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemSelectTicket;
	JMenuItem mnuItemHelp;
	
	JTable jt;

	//Create a new method called tickets that checks whether the login user is an admin or user
	public Tickets(String user) {
		//If the user is an admin, make chkIfAdmin true and create and admin menu and gui
		if (user.equals("admin")) {
			chkIfAdmin = true;
			AdminMenu();
			AdminGUI();
		}
		//If the user is not an admin, create a user admin menu and gui
		else {
			UserMenu();
			UserGUI();
		}
	}

	//Create a new method called AdminMenu that creates the menu for an admin
	private void AdminMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);
		
		mnuItemRefresh = new JMenuItem("Refresh");
		mnuFile.add(mnuItemRefresh);
		
		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);
		

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemRefresh.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
	}
	
	//Create a new method called UserMenu to create the menu for the user
	private void UserMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);
		
		mnuItemHelp = new JMenuItem("Help");
		mnuFile.add(mnuItemHelp);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);
		
		mnuItemSelectTicket = new JMenuItem("View Your Ticket");
		mnuTickets.add(mnuItemSelectTicket);

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemHelp.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemSelectTicket.addActionListener(this);
	}

	//Create a new method called AdminGUI to create the GUI for the admin
	private void AdminGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(800, 500);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//Create a new method called UserGUI to create the GUI for the user
	private void UserGUI() {
		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(800, 500);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	//Create the action performed method for the actions when each menu is pressed
	public void actionPerformed(ActionEvent e) {
		//If the exit button is pressed do this
		if (e.getSource() == mnuItemExit) {
			//Exit the system
			System.exit(0);
		} 
		//If the Open Ticket Button is pressed do this
		else if (e.getSource() == mnuItemOpenTicket) {

			//Get the ticketName and the ticketDescription
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			//Insert the ticket information into the records table
			int id = dao.insertRecords(ticketName, ticketDesc);
			
			//Tell the user whether the ticket creation that was a success or failure
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} 
			else if (id == 0){
				System.out.println("Ticket cannot be created!!!");
			}
		}
		//If the help button is pressed do this
		else if (e.getSource() == mnuItemHelp) {
			//Ask the user to enter a contact phone number and check whether it is correct or not
			String phoneNumber = JOptionPane.showInputDialog(null, "Enter a phone number that we can contact you?");
			int confirm = JOptionPane.showConfirmDialog(null, "Is this phone number correct? " + phoneNumber);
			//If the number is correct tell the user that they will be contacted
			if (confirm == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "You will be contacted as soon as a representative becomes available");
			}
			//If the number is not correct have them enter a new number
			else if (confirm == JOptionPane.NO_OPTION) {
				String newNumber = JOptionPane.showInputDialog(null, "Please enter the correct number");
				phoneNumber = newNumber;
			}
		}
		//If the user presses the View Ticket or Refresh button
		else if (e.getSource() == mnuItemViewTicket) {

			//Retrieve all of the data from the tables
			try {
				///Create a JTable using all of the ticket data from the records
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				//Color the background and the table header
				jt.setBackground(Color.PINK);
				jt.setForeground(Color.BLACK);
				jt.getTableHeader().setBackground(Color.BLUE);
				jt.getTableHeader().setForeground(Color.WHITE);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen
				//If it is View Ticket, tell the user that the ticket view was created
				JOptionPane.showMessageDialog(null, "The Ticket View was created");
				System.out.println("Ticket View was successfully created");
			//If it does not work, throw an exception
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == mnuItemRefresh) {
			try {
				new Tickets("admin").mnuItemViewTicket.doClick();
				setVisible(false);
				dispose();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "The Table was refreshed");
			System.out.println("The table was successfully refreshed");
		}
		//If the Select Ticket button is pressed do this
		else if (e.getSource() == mnuItemSelectTicket) {
			//Ask the user what ticket they would like to select and parse it into an int
			String tick_Id = JOptionPane.showInputDialog(null, "Enter your selected Ticket ID number");
			int tid = Integer.parseInt(tick_Id);
			//Create a try block 
			try {
				//Create a JTable using all of the ticket data from the records 
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.selectRecords(tid)));
				jt.setBounds(30, 40, 200, 400);
				//Color the background and the table header
				jt.setBackground(Color.MAGENTA);
				jt.setForeground(Color.WHITE);
				jt.getTableHeader().setBackground(Color.WHITE);
				jt.getTableHeader().setForeground(Color.BLACK);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				//Refresh or repaint the frame on the screen
				setVisible(true);
				//Tell the user that the view of their selected ticket was a success
				JOptionPane.showMessageDialog(null, "The Selected Ticket was successfully viewed");
				System.out.println("Selected Ticket View was a success");
			}
			//If it does not work, throw an exception
			catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		//If the update button is pressed, do this
		else if (e.getSource() == mnuItemUpdate) {
			//Enter the ticket number for the ticket to update and parse that int
			String tick_Id = JOptionPane.showInputDialog(null, "Enter the ticket id for the ticket you wish to update");
			int tid = Integer.parseInt(tick_Id);
			//Update the record using the id
			dao.updateRecords(tid);
			//If it is 0, tell the user that the ticket could not be updated
			if (tid == 0) {
				JOptionPane.showMessageDialog(null, "The following ticket could not be updated");
			}
		}
		//If the delete button is pressed, do this
		else if (e.getSource() == mnuItemDelete) {
			//Enter the number of the ticket id that you want to delete and parse that String into an int
			String tick_Id = JOptionPane.showInputDialog(null, "Enter the ticket id you want to delete");
			int tid = Integer.parseInt(tick_Id);
			//Ask the user on whether or not they want to delete the ticket
			int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ticket " + tid, "Warning once you delete it, it cannot be recovered", JOptionPane.YES_NO_OPTION);
			//If they say Yes, delete the record
			if (response == JOptionPane.YES_OPTION) {
				int id = dao.deleteRecords(tid);
				//If the id is not 0 tell them that it was a success 
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " was successfully deleted");
					JOptionPane.showMessageDialog(null, "Ticket id " + id + " was deleted");
				}
				//If not, tell the user it could not be updated
				else {
					JOptionPane.showMessageDialog(null, "The ticket could not be deleted");
				}
			}
			//If they say no, tell the user it was not deleted
			else if (response == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(null, "The following ticket: " + tid + " was not deleted");
			}
			//If they cancel the message, tell them that the deletion was cancelled
			else if (response == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "The deletion request of ticket: " + tid + " was cancelled");
			}
		}
	}
}
