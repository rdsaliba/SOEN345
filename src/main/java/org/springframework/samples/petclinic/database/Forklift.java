package org.springframework.samples.petclinic.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collection;

public class Forklift {

    private static Logger log = LoggerFactory
        .getLogger(Forklift.class);

    private static Connection connMySQL = null;
    private static Connection connPostgres = null;
    private static ResultSet resultSet = null;
    private static Statement statementPostgres = null;

    // Will rename this to forklift() so that we can just call it elsewhere
    public static void main(String [] args){

        try{
            setUpConnection();

            // Migrate Scheme from MySQL to Postgres
            // This will read from schema
            String queryScript = new String(Files.readAllBytes(Paths.get("src//main//resources//db//postgres//schema.sql")));
            statementPostgres = connPostgres.createStatement() ;
            ResultSet resultSet = statementPostgres.executeQuery(queryScript);

        }catch (Exception ce){
            log.info("Unexpected Exception");
        }

        String[] tables = {"owners", "types", "pets", "specialties", "vets", "visits", "vet_specialties"};

        try{
            for(String eachTable: tables)
            {
                try{
                    String dataToMigrate = getInsertIntoValues(eachTable);
                    statementPostgres = connPostgres.createStatement() ;
                    String query1 =
                        "INSERT INTO " + eachTable + " VALUES " + dataToMigrate;
                    System.out.println(query1);
                    resultSet = statementPostgres.executeQuery(query1);
                } catch (SQLException ce){
                    log.info("ClassNotFoundException exception");
                }catch (Exception ce){
                    log.info("Unexpected Exception");
                }                String dataToMigrate = getInsertIntoValues(eachTable);
            }

            resultSet.close();

        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }

    }
/*
    private static Collection<String> getTables(){

        Collection<String> tables = null;
        try{
            Statement statement = connMySQL.createStatement() ;

            String query1 =
                "SHOW TABLES";
            ResultSet resultSet = statement.executeQuery(query1);

            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
                System.out.print(resultSet.getString(1)+"\n");
            }
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }

       return tables;
    }
*/
    private static ResultSet getTableData(String tableName){

        Collection<String> tables = null;
        try{
            Statement statement = connMySQL.createStatement();
            String query1 =
                "SELECT * FROM " + tableName +";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }

        return resultSet;
    }

    private static void setUpConnection(){
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

    private static String getInsertIntoValues(String tableName){
        ResultSet temp = getTableData(tableName);
        String numValue="";

        try{
            ResultSetMetaData rsmd = temp.getMetaData();
            int rowCount = 0;
            while (temp.next())
            {
                ++rowCount;
            }
            temp.beforeFirst();
            int columnCount = rsmd.getColumnCount();

            String testing[][] = new String[rowCount][columnCount];

            int tempRow = 1;
            for (int x = 1; x <= columnCount; ++x) {
                while(temp.next())
                {
                    testing[tempRow-1][x-1] = "\'" + temp.getString(x) + "\'";
                    tempRow++;
                }
                tempRow=1;
                temp.beforeFirst();
            }

            for(int i=0; i<rowCount; i++) {
                numValue+="(";

                for(int j=0; j<columnCount; j++) {
                    numValue +=testing[i][j];

                    if(j != (columnCount-1)) {
                        numValue +=", ";
                    }
                }
                numValue +=")";

                if(i != rowCount-1) {
                    numValue +=", ";
                }else {
                    numValue +=";";
                }
            }

        } catch (SQLException se){
            log.info("SQLException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }

        return  numValue;
    }
}
