package org.springframework.samples.petclinic.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CleanOldDatabase {

	private  Connection connMySQL = null;

	public CleanOldDatabase(){
        SetupConnectionTwoDb setupConnectionTwoDb = SetupConnectionTwoDb.getSteupConnectionTwoDbInstance();
        connMySQL = SetupConnectionTwoDb.connMySQL;

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
