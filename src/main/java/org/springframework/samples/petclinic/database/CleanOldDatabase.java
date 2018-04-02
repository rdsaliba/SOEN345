package org.springframework.samples.petclinic.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CleanOldDatabase {

	private  Connection connMySQL = null;

	public CleanOldDatabase(){
        setUpConnection();

    }

	private void setUpConnection(){
        String databaseNameMySQL = "petclinic";
        String userNameMySQL = "root";
        String passwordMySQL = "test";
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
		try{
            Statement statement = connMySQL.createStatement();
            String query1 = "DROP DATABASE petclinic";
            statement.executeUpdate(query1);
        }catch (SQLException ce){
        }catch (Exception ce){
        }
	}
}
