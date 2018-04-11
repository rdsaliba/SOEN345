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
        SetupConnectionTwoDb setupConnectionTwoDb = SetupConnectionTwoDb.getSteupConnectionTwoDbInstance();
        connMySQL = SetupConnectionTwoDb.connMySQL;
        connPostgres = SetupConnectionTwoDb.connPostgres;
        resultSet = SetupConnectionTwoDb.resultSet;
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

    // Use connection type to get table data from either postgres or mysql
    private ResultSet getBackupTableData(String tableName, Connection connection){

        Collection<String> tables = null;
        try{
            Statement statement = connection.createStatement();
            String query1 =
                "SELECT hash FROM backup_hash WHERE table_name="+tableName+ " ORDER BY row_number ASC;";
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

    // Get Postgres table rows for table
    public String[][] getBackupDataHash(String tableName){
        ResultSet temp = null;

        temp = getBackupTableData(tableName, connPostgres);

        String numValue="";
        String testing[][] = null;

        try{
            ResultSetMetaData rsmd = getBackupTableData(tableName, connPostgres).getMetaData();
            int rowCount = 0;
            while (temp.next())
            {
                ++rowCount;
            }
            int columnCount = rsmd.getColumnCount();
            String columnName = rsmd.getColumnName(1);
            testing = new String[rowCount][columnCount];
            temp = getBackupTableData(tableName, connPostgres);

            int tempRow = 1;
            for (int x = 1; x <= columnCount; ++x) {
                while(temp.next())
                {
                    testing[tempRow-1][x-1] = "\'" + temp.getString(x) + "\'";
                    tempRow++;
                }
                tempRow=1;
                temp = getBackupTableData(tableName, connPostgres);;
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
            int count = statement.executeUpdate(query1);
            if(!(count>0)){
                String query2 =
                    "INSERT INTO owners VALUES (" + id + ", " + first_name  + ", " + last_name + ", " + address + ", " + city + ", " + telephone + ");";
                statement.executeUpdate(query2);
            }
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
            int count = statement.executeUpdate(query1);
            if(!(count>0)){
                String query2 =
                    "INSERT INTO pets VALUES("+ id +", " + name + ", " + birth_date + ", " + type_id + ", " + owner_id+ ");";
                statement.executeUpdate(query2);
            }
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
            int count = statement.executeUpdate(query1);
            if(!(count>0)){
                String query2 =
                    "INSERT INTO specialties VALUES("+ id +", " + name + ");";
                statement.executeUpdate(query2);
            }
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
            int count = statement.executeUpdate(query1);
            if(!(count>0)){
                String query2 =
                    "INSERT INTO types VALUES("+ id +", " + name + ");";
                statement.executeUpdate(query2);
            }
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
                "UPDATE vets SET first_name=" + first_name + " , last_name =" + last_name + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
            int count = statement.executeUpdate(query1);
            if(!(count>0)){
                String query2 =
                    "INSERT INTO vets VALUES("+ id +", " + first_name +", " + last_name + ");";
                statement.executeUpdate(query2);
            }
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
                "UPDATE visit SET pet_id =" + pet_id + " , visit_date=" + visit_date + " , description=" + description + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
            int count = statement.executeUpdate(query1);
            if(!(count>0)){
                String query2 =
                    "INSERT INTO visit VALUES("+ id +", " + pet_id +", " + visit_date +", " + description + ");";
                statement.executeUpdate(query2);
            }
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void insertHashBackup(String tableName, String rowNumber, String hashBackup){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "INSERT INTO backup_hash VALUES (\'" + tableName + "\', " + rowNumber + ", \'" + hashBackup + "\');";
            statement.executeUpdate(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception" + ":" +  ce);
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }
}
