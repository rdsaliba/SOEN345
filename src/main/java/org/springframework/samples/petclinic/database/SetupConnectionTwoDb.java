package org.springframework.samples.petclinic.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetupConnectionTwoDb {

    private static Logger log = LoggerFactory.getLogger(SetupConnectionTwoDb.class);

    public static Connection connMySQL = null;
    public static Connection connPostgres = null;
    public static ResultSet resultSet = null;
    private static SetupConnectionTwoDb setupConnectionTwoDb = null;

    private SetupConnectionTwoDb() {
    }

    public static SetupConnectionTwoDb getSteupConnectionTwoDbInstance(){
        if(setupConnectionTwoDb == null){
            setupConnectionTwoDb = new SetupConnectionTwoDb();
            setUpConnection();
        }
        if(connMySQL == null || connPostgres == null){
            setUpConnection();
        }

        return setupConnectionTwoDb;
    }

    // Setup Connection to postgres and mysql
    public static void setUpConnection(){
        String databaseNameMySQL = "petclinic";
        String userNameMySQL = "root";
        String passwordMySQL = "test";
        String mySQLPort = "3306";

        String databaseNamePostgres = "petclinic";
        String userNamePostgres = "postgres";
        String passwordPostgres = "test";
        String postgresPort = "5432";

        String hostUrl = "localhost";

        try{
            // Setup the connection with the MySQL DB
            Class.forName("com.mysql.jdbc.Driver");
            connMySQL = DriverManager.getConnection("jdbc:mysql://" + hostUrl
                + ":" + mySQLPort + "/petclinic", userNameMySQL, passwordMySQL);

            // Setup the connection with the MySQL DB
            Class.forName("org.postgresql.Driver");
            connPostgres = DriverManager.getConnection("jdbc:postgresql://" + hostUrl
                + ":" + postgresPort + "/postgres?currentSchema=petclinic", userNamePostgres, passwordPostgres);
        } catch (ClassNotFoundException ce){
            log.info("ClassNotFoundException exception 1");
        }catch (Exception ce){
            log.info("Unexpected Exception 1");
        }
    }

    public static void closeConnection(){
        try{
            connPostgres.close();
            connMySQL.close();
            resultSet.close();
        }catch (SQLException se){
            log.info("ClassNotFoundException exception 1");
        }catch (Exception ce){
            log.info("Unexpected Exception 1");
        }
    }
}
