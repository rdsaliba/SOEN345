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
        try {
	        String[] tables = {"owners", "types", "pets", "specialties", "vets", "visits", "vet_specialties"};
	    	for(String eachTable: tables) {
	    		   	String newData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerPostgres(eachTable);
	    	        String oldData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerMySQL(eachTable);
	    	        ConsistencyChecker consistencyChecker = new ConsistencyChecker(oldData, newData);
	    	        try{
	    	            consistencyChecker.checkConsistency(eachTable);
	    	        }catch (Exception e){

	    	        }
	    	}
        }catch(Exception e){

        }
    	
     
    }
}
