package org.springframework.samples.petclinic.system;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class MigrationTests {

    private static final int TEST_OWNER_ID = 1;
    
    @Autowired
    private OwnerRepository owners;

    private Owner owner;
    
    @Before
    public void setup() {
    	owner = new Owner();
    	owner.setId(TEST_OWNER_ID);
    	owner.setFirstName("test");
    	owner.setLastName("test");
    	owner.setAddress("110 W. Liberty St.");
    	owner.setCity("Madison");
    	owner.setTelephone("6085551023");
    }
    
    @Test
    public void testFindOwner() throws Exception {
    	//If databases are correctly configures this will print two owners with different addresses.
    	//Must have user test in database
    	System.out.println(owners.findByLastName(owner.getLastName())); //served from mysql
    	DatabaseThreadContext.setCurrentDatabase(Database.SECONDARY); //switched database to postgres
    	System.out.println(owners.findByLastName(owner.getLastName())); // served from postgres
    }
}
