package org.springframework.samples.petclinic.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collection;

public class ConsistencyCheckerUpdate {

    private static Logger log = LoggerFactory
        .getLogger(ConsistencyCheckerUpdate.class);

    private  Connection connMySQL = null;
    private  Connection connPostgres = null;
    private  ResultSet resultSet = null;

    public ConsistencyCheckerUpdate(){
        setUpConnection();

    }

    // Setup Connection to postgres and mysql
    private void setUpConnection(){
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

    // Use connection type to get table data from either postgres or mysql
    private ResultSet getTableData(String tableName, Connection connection){

        Collection<String> tables = null;
        try{
            Statement statement = connection.createStatement();
            String query1 =
                "SELECT * FROM " + tableName +" ORDER BY id ASC;";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }

        return resultSet;
    }

    // Get MySQL table rows for table
    public String[][] getInsertIntoValuesForConsistencyCheckerMySQL(String tableName){
        ResultSet temp = null;

        temp = getTableData(tableName, connMySQL);

        String numValue="";
        String testing[][] = null;

        try{
            ResultSetMetaData rsmd = temp.getMetaData();
            int rowCount = 0;
            while (temp.next())
            {
                ++rowCount;
            }
            temp.beforeFirst();
            int columnCount = rsmd.getColumnCount();
            String columnName = rsmd.getColumnName(1);

            testing = new String[rowCount][columnCount];

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

        } catch (SQLException se){
            log.info("SQLException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        } finally {

            return testing;

        }
    }

    // Get Postgres table rows for table
    public String[][] getInsertIntoValuesForConsistencyCheckerPostgres(String tableName){
        ResultSet temp = null;

        temp = getTableData(tableName, connPostgres);

        String numValue="";
        String testing[][] = null;

        try{
            ResultSetMetaData rsmd = getTableData(tableName, connPostgres).getMetaData();
            int rowCount = 0;
            while (temp.next())
            {
                ++rowCount;
            }
            int columnCount = rsmd.getColumnCount();
            String columnName = rsmd.getColumnName(1);
            testing = new String[rowCount][columnCount];
            temp = getTableData(tableName, connPostgres);

            int tempRow = 1;
            for (int x = 1; x <= columnCount; ++x) {
                while(temp.next())
                {
                    testing[tempRow-1][x-1] = "\'" + temp.getString(x) + "\'";
                    tempRow++;
                }
                tempRow=1;
                temp = getTableData(tableName, connPostgres);;
            }


        } catch (SQLException se){
            log.info("SQLException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        } finally {

            return testing;
        }
    }

    // Method to update owners in Postgres
    public void updateOwners(String id, String first_name, String last_name, String address, String city, String telephone){

        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE owners SET first_name=" + first_name + " , last_name =" + last_name + " , address=" + address + " , city=" + city + " , telephone=" + telephone + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    // Method to update pets in Postgres
    public void updatePets(String id, String name, String birth_date, String type_id, String owner_id){

        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE pets SET name=" + name + " , birth_date =" + birth_date + " , type_id=" + type_id + " , owner_id=" + owner_id + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    // Method to update specialties in Postgres
    public void updateSpecialities(String id, String name){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE specialties SET name=" + name + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    // Method to update types in Postgres
    public void updateTypes(String id, String name){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE types SET name=" + name + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    // Method to update vets in Postgres
    public void updateVets(String id, String first_name, String last_name){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE owners SET first_name=" + first_name + " , last_name =" + last_name + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    // Method to visit owners in Postgres
    public void updateVisit(String id, String pet_id, String visit_date, String description){

        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE pets SET pet_id =" + pet_id + " , visit_date=" + visit_date + " , description=" + description + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void updateHashBackup(String tableName, String rowNumber, String hashBackup){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "INSERT INTO backup_hash VALUE (" + tableName + ", " + rowNumber + ", " + hashBackup + ");";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }
}
