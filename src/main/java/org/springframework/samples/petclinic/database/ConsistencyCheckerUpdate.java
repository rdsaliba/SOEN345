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
    private  Statement statementPostgres = null;

    public ConsistencyCheckerUpdate(){
        setUpConnection();

    }

    private ResultSet getTableData(String tableName, Connection connection){

        Collection<String> tables = null;
        try{
            Statement statement = connection.createStatement();
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

    private void setUpConnection(){
        String databaseNameMySQL = "petclinic";
        String userNameMySQL = "root";
        String passwordMySQL = "pizza123";
        String mySQLPort = "3306";

        String databaseNamePostgres = "petclinic";
        String userNamePostgres = "postgres";
        String passwordPostgres = "laxman123";
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

    /* "MySQL or Postgres*/
    public String[][] getInsertIntoValuesForConsistencyChecker(String tableName, String DB){
        ResultSet temp = null;

        if(DB.equals("MySQL")){

            temp = getTableData(tableName, connMySQL);

        }else if(DB.equals("Postgres"))
        {
            temp = getTableData(tableName, connPostgres);
        }

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
        }

        return  testing;
    }

    public void updateOwners(String id, String first_name, String last_name, String address, String city, String telephone){

        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE owners SET first_name=\'" + first_name + "\' AND last_name =\'" + last_name + "\' AND address=\'" + address + "\' AND city=\'" + city + "\' AND telephone=\'" + telephone + "\' WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void updatePets(String id, String name, String birth_date, String type_id, String owner_id){

        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE pets SET name=\'" + name + "\' AND birth_date =\'" + birth_date + "\' AND type_id=" + type_id + " AND owner_id=" + owner_id + " WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void updateSpecialities(String id, String name){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE specialties SET name=\'" + name + "\' WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void updateTypes(String id, String name){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE types SET name=\'" + name + "\' WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void updateVets(String id, String first_name, String last_name){
        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE owners SET first_name=\'" + first_name + "\' AND last_name =\'" + last_name + "\' WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }

    public void updateVisit(String id, String pet_id, String visit_date, String description){

        try{
            Statement statement = connPostgres.createStatement();
            String query1 =
                "UPDATE pets SET pet_id =" + pet_id + " AND visit_date=\'" + visit_date + "\' AND description=\'" + description + "\' WHERE id=" + id + ";";
            resultSet = statement.executeQuery(query1);
        } catch (SQLException ce){
            log.info("ClassNotFoundException exception");
        }catch (Exception ce){
            log.info("Unexpected Exception");
        }
    }
}
