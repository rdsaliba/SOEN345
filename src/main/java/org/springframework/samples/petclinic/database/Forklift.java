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

    private  Connection connMySQL = null;
    private  Connection connPostgres = null;
    private  ResultSet resultSet = null;
    private  Statement statementPostgres = null;

    // Will rename this to forklift() so that we can just call it elsewhere
    public void forklift(){

        try{
            SetupConnectionTwoDb setupConnectionTwoDb = SetupConnectionTwoDb.getSteupConnectionTwoDbInstance();
            connMySQL = SetupConnectionTwoDb.connMySQL;
            connPostgres = SetupConnectionTwoDb.connPostgres;
            resultSet = SetupConnectionTwoDb.resultSet;

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
    private String getInsertIntoValues(String tableName){
        ResultSet temp = getTableData(tableName, connMySQL);
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
            String columnName = rsmd.getColumnName(1);

            String testing[][] = new String[rowCount][columnCount];

            int tempRow = 1;
            for (int x = 1; x <= columnCount; ++x) {
                while(temp.next())
                {
                    if(x==1 && columnName.equals("id")) {
                        testing[tempRow-1][x-1] = "DEFAULT";
                        tempRow++;
                        continue;
                    }
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
