/*
 * Name: Cheryl Gardner 
 * Date: 12/10/2020
 * Program: ticketsJTable.java
 * ITMD 411 Final Project
 * Purpose: To create the format for the table models of the JTables that is used to display all of the data of the tables
 */

//Import the javaapplication1 package into the program
package javaapplication1;

//Import all of the necessary packages into the program
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
 
//Create a new class called ticketsJTable that formats the JTables
public class ticketsJTable {

	@SuppressWarnings("unused")
	//Create a new final table model and build of that table model
	private final DefaultTableModel tableModel = new DefaultTableModel();

	//Decare a new method called DefaultTableModel that displays the results to the user and throws SQL Exceptions
	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		//Get all of the meta data from the tables. 
		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}
		// return data/col.names for JTable
		return new DefaultTableModel(data, columnNames); 
	}
}
