package org.springframework.samples.petclinic.database;

public class Migration {

    public static void main(String[] args) {

        Forklift forklift = new Forklift();
        forklift.forklift();


        ConsistencyCheckerUpdate consistencyCheckerUpdate = new ConsistencyCheckerUpdate();
        String temp[][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyChecker("owners", "MySQL");

        for(int i=0; i<temp.length; i++) {

            for(int j=0; j<temp[0].length; j++) {

                System.out.println(temp[i][j] + " ");

            }
        }






    }
}
