package org.springframework.samples.petclinic.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class Migration {

    private static Logger log = LoggerFactory
        .getLogger(Migration.class);

    public static void main(String[] args) {

        Forklift forklift = new Forklift();
        forklift.forklift();


        ConsistencyCheckerUpdate consistencyCheckerUpdate = new ConsistencyCheckerUpdate();

        int count = 0;
        double totalThreshold = 0;
        ConsistencyChecker consistencyChecker;
        try {
	        String[] tables = {"owners", "types", "pets", "specialties", "vets", "visits"};
	    	for(String eachTable: tables) {

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

        System.out.println("The treshold level of all the tables is: " + totalThreshold/count * 100);

        // Remove everything from old database
        if(totalThreshold/count > 0.99) {
            CleanOldDatabase cleanDbOld = new CleanOldDatabase();
            // Comment out to not remove the MySQL database for now
            //  cleanDbOld.removeOldData();
        }

        // Run Consistency Checker every 15 min
        Timer timer = new Timer();
        timer.schedule(new ScheduledConsistencyChecker(), 0, 900000);
    }
}
