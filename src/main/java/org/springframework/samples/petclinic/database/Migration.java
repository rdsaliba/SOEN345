package org.springframework.samples.petclinic.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Migration {

    private static Logger log = LoggerFactory
        .getLogger(Migration.class);

    public static void main(String[] args) {

        Forklift forklift = new Forklift();
        forklift.forklift();


        ConsistencyCheckerUpdate consistencyCheckerUpdate = new ConsistencyCheckerUpdate();
        /*
        for(int i=0; i<temp.length; i++) {

            for(int j=0; j<temp[0].length; j++) {

                System.out.println(temp[i][j] + " ");

            }
        }
        */
        int count = 0;
        double totalThreshold = 0;
        ConsistencyChecker consistencyChecker;
        try {
	        String[] tables = {"owners", "types", "pets", "specialties", "vets", "visits"};
	    	for(String eachTable: tables) {
                System.out.println("The new data is: " + eachTable);

                String newData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerPostgres(eachTable);
                String oldData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerMySQL(eachTable);

                consistencyChecker = new ConsistencyChecker(oldData, newData);
                count++;
                try{
                    consistencyChecker.checkConsistency(eachTable);
                    totalThreshold += consistencyChecker.getThresholdLevel();
                }catch (Exception e){

                }

	    	}
        }catch(Exception e){

        }
        // Remove everything from old database
        if(totalThreshold/count > 0.99) {
        CleanOldDatabase cleanDbOld = new CleanOldDatabase();

        // Comment out to not remove the MySQL database for now
        cleanDbOld.removeOldData();
        }
    }
}
