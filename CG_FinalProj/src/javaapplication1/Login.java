/*
 * Name: Cheryl Gardner 
 * Date: 12/10/2020
 * Program: Login.java
 * ITMD 411 Final Project
 * Purpose: Create a new Login menu that checks to make sure that the user is valid 
 * and to create all of the necessary login buttons and usernames
 */

//Import the necessary package
package javaapplication1;

//Import all of the necessary packages for the program
import java.awt.Color;
import java.awt.GridLayout; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
//Create a new class called Login that extends JFrame
public class Login extends JFrame {

	//Declare a new Dao connection variable to be used later
	Dao conn;

	public Login() {

		//Create the window header 
		super("IIT HELP DESK LOGIN");
		//Call the Dao program and the create table method
		conn = new Dao();
		conn.createTables();
		//Set the size, grid layout and location for the login window
		setSize(800, 500);
		setLayout(new GridLayout(5, 2));
		setLocationRelativeTo(null); 

		//Color the overall login content pane
		getContentPane().setBackground(Color.CYAN);
		
		//Create a new Header that tells the users what this program us
		JLabel header1 = new JLabel("Cheryl's Help ", JLabel.RIGHT);
		JLabel header2 = new JLabel("Desk System", JLabel.LEFT);
		
		//Color the text for the headers
		header1.setForeground(Color.BLACK);
		header2.setForeground(Color.BLACK);
		
		//Create the login and passwords labels
		JLabel lblUsername = new JLabel("Username", JLabel.LEFT);
		JLabel lblPassword = new JLabel("Password", JLabel.LEFT);
		
		//Color the text for the labels
		lblUsername.setForeground(Color.BLACK);
		lblPassword.setForeground(Color.BLACK);
		
		//Create a status bar at the bottom and create a password and text field for users to input their credentials 
		JLabel lblStatus = new JLabel(" ", JLabel.CENTER);
		// JLabel lblSpacer = new JLabel(" ", JLabel.CENTER);
		JTextField txtUname = new JTextField(10);
		JPasswordField txtPassword = new JPasswordField();
		
		//Color the fields with the background to blend in and color the text that is inputed
		txtUname.setBackground(Color.CYAN);
		txtPassword.setBackground(Color.CYAN);
		txtUname.setForeground(Color.BLACK);
		txtPassword.setForeground(Color.BLACK);
		
		//Add two buttons one to submit and one to exit
		JButton btn = new JButton("Submit");
		JButton btnExit = new JButton("Exit");
		
		//Color the background of the buttons as well as the text
		btn.setBackground(Color.BLUE);
		btnExit.setBackground(Color.RED);
		btn.setOpaque(true);
		btn.setBorderPainted(false);
		btnExit.setOpaque(true);
		btnExit.setBorderPainted(false);
		btn.setForeground(Color.WHITE);
		btnExit.setForeground(Color.WHITE);

		//Create the necessary constraints
		lblStatus.setToolTipText("Contact help desk to unlock password");
		lblUsername.setHorizontalAlignment(JLabel.CENTER);
		lblPassword.setHorizontalAlignment(JLabel.CENTER);
 
		//Add all of the Objects into the frame
		add(header1); //1st row
		add(header2);
		add(lblUsername);//2nd row
		add(txtUname);
		add(lblPassword); //3rd row
		add(txtPassword);
		add(btn); //4th row
		add(btnExit);
		add(lblStatus); //5th row

		//Add a new button listener when the submit button is pressed
		btn.addActionListener(new ActionListener() {
			//Declare the count to be 0
			int count = 0; // count agent

			@Override
			//Add an addition actionEvent to check the user's credentials
			public void actionPerformed(ActionEvent e) {
			//Declare the user to be empty and add one to the count
			String user = "";
			count = count + 1;
			
				//Create a prepared statement that checks the username and password of the user
				String query = "SELECT * FROM cgard_users1 WHERE uname = ? and upass = ?;";
				try (PreparedStatement stmt = conn.getConnection().prepareStatement(query)) {
					//Set the two fields to be the inputted username and password
					stmt.setString(1, txtUname.getText());
					stmt.setString(2, txtPassword.getText());
					//Execute the query
					ResultSet rs = stmt.executeQuery();
					//If there is a next in the result set
					if (rs.next()) {
						//Check the admin field to be if it is 1 or 0
						int isAdmin = rs.getInt("admin");
						//If it is an admin set the user to be admin and welcome the admin
						if (isAdmin == 1) {
							user = "admin";
							JOptionPane.showMessageDialog(null, "Welcome Admin");
						}
						//If it is a regular user, welcome the user into the system
						else if (isAdmin != 1) {
							JOptionPane.showMessageDialog(null, "Welcome User");
						}
						//Pass the user into the tickets progra,
						new Tickets(user);
						//Hide the frame and close out of the window
						setVisible(false); 
						dispose(); 
					} 
					//If the login is not right, update the login saying how many attempts are left and update status
					else
						lblStatus.setText("Try again! " + (3 - count) + " / 3 attempts left");
				} 
				//If the try block does not work, throw the error to the user
				catch (SQLException ex) {
					ex.printStackTrace();
				}
 			 
			}
		});
		//Add an action listener for the exit
		btnExit.addActionListener(e -> System.exit(0));
		setVisible(true); 
	}

	//Call the main method
	public static void main(String[] args) {
		//Call the new Login program to run
		new Login();
	}
}
