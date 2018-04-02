package org.springframework.samples.petclinic.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class CleanOldDatabase {
		
	private  Connection connMySQL = null;
	
	public CleanOldDatabase(){
        setUpConnection();

    }
		 
	private void setUpConnection(){
        String databaseNameMySQL = "petclinic";
        String userNameMySQL = "root";
        String passwordMySQL = "12345";
        String mySQLPort = "3306";
        
        String hostUrl = "localhost";

        try{

            // Setup the connection with the MySQL DB
            Class.forName("com.mysql.jdbc.Driver");
            connMySQL = DriverManager.getConnection("jdbc:mysql://" + hostUrl
                + ":" + mySQLPort + "/petclinic", userNameMySQL, passwordMySQL);
        }catch (Exception ce) {}
	}
	
	public void removeOldData() {
		
	}
}
