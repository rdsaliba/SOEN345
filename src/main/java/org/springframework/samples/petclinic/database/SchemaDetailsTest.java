package org.springframework.samples.petclinic.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaDetailsTest {

	private static Logger log = LoggerFactory
			.getLogger(SchemaDetailsTest.class);
	
	public static void main(String args[]) throws Exception {
		String databaseName = "petclinic";
		String userName = "root";
		String password = "root";
		String mySQLPort = "8080";
		String hostUrl = "localhost:3306/petclinic";
		
		// Setup the connection with the DB
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostUrl
				+ ":" + mySQLPort, userName, password);
		
		// --- LISTING DATABASE SCHEMA NAMES ---
		ResultSet resultSet = conn.getMetaData().getCatalogs();
		while (resultSet.next()) {
			log.info("Schema Name = " + resultSet.getString("TABLE_CAT"));
		}
		resultSet.close();
		
		// --- LISTING DATABASE TABLE NAMES ---
		String[] types = { "TABLE" };
		resultSet = conn.getMetaData()
				.getTables(databaseName, null, "%", types);
		String tableName = "";
		while (resultSet.next()) {
			tableName = resultSet.getString(3);
			log.info("Table Name = " + tableName);
		}
		resultSet.close();
		
		// --- LISTING DATABASE COLUMN NAMES ---
		DatabaseMetaData meta = conn.getMetaData();
		resultSet = meta.getColumns(databaseName, null, tableName, "%");
		while (resultSet.next()) {
			log.info("Column Name of table " + tableName + " = "
					+ resultSet.getString(4));
		}
	}
}
