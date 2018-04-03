package org.springframework.samples.petclinic.database;

import java.util.TimerTask;

public class ScheduledConsistencyChecker extends TimerTask{
    public void run(){

        ConsistencyChecker consistencyChecker;
        try {
            String[] tables = {"owners", "types", "pets", "specialties", "vets", "visits"};
            for (String eachTable : tables) {

                ConsistencyCheckerUpdate consistencyCheckerUpdate = new ConsistencyCheckerUpdate();

                String newData[][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerPostgres(eachTable);
                String oldData[][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerMySQL(eachTable);

                consistencyChecker = new ConsistencyChecker(oldData, newData);
                try {
                    consistencyChecker.checkConsistency(eachTable);
                } catch (Exception e) {

                }

            }
        } catch (Exception e) {

        }
    }
}
