package org.springframework.samples.petclinic.system;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.samples.petclinic.database.ConsistencyChecker;
import org.springframework.samples.petclinic.database.ConsistencyCheckerUpdate;
import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.VisitService;
import org.springframework.samples.petclinic.vet.VetService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class MigrationTests {

    private static final int TEST_OWNER_ID = 100;
    
    @Autowired
    private OwnerRepository owners;
    
    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private VisitService visitService;
    
    @Autowired
    private VetService vetService;

    private Owner owner;
    private ConsistencyChecker cc;
    private ConsistencyCheckerUpdate consistencyCheckerUpdate;
    
    @Before
    public void setup() {
    	consistencyCheckerUpdate = new ConsistencyCheckerUpdate();
        String newData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerPostgres("Owners");
        String oldData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerMySQL("Owners");
    	cc= new ConsistencyChecker(oldData, newData);
    	
    	owner = new Owner();
    	owner.setId(TEST_OWNER_ID);
    	owner.setFirstName("test");
    	owner.setLastName("test");
    	owner.setAddress("110 W. Liberty St.");
    	owner.setCity("Madison");
    	owner.setTelephone("6085551023");
    	
        PetType cat = new PetType();
        cat.setId(3);
        cat.setName("hamster");
    }
    
    @Test
    @Transactional
    public void testConsistency() throws Exception {
        String newData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerPostgres("Owners");
        String oldData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerMySQL("Owners");
    	cc= new ConsistencyChecker(oldData, newData);
    	//Checks if new and old database insert owner correctly
    	int id = ownerService.saveNew(Database.PRIMARY, owner);
    	int id2 = ownerService.saveNew(Database.SECONDARY, owner);
    	assertEquals(0, cc.checkConsistency("Owners"));
    	
    	//Checks if new and old database update owner correctly
    	owner.setId(id);
    	owner.setAddress("new address");
    	ownerService.update(Database.PRIMARY, owner);
    	ownerService.update(Database.SECONDARY, owner);
    	assertEquals(0, cc.checkConsistency("Owners"));
    	
//      String newData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerPostgres("Pets");
//      String oldData [][] = consistencyCheckerUpdate.getInsertIntoValuesForConsistencyCheckerMySQL("Pets");
//    	cc= new ConsistencyChecker(oldData, newData);
//    	//Checks if new and old database insert owner correctly
//    	int id = ownerService.saveNew(Database.PRIMARY, owner);
//    	int id2 = ownerService.saveNew(Database.SECONDARY, owner);
//    	assertEquals(0, cc.checkConsistency("Owners"));
//    	assertEquals(id, id2);
//    	
//    	//Checks if new and old database update owner correctly
//    	owner.setId(id);
//    	owner.setAddress("new address");
//    	ownerService.update(Database.PRIMARY, owner);
//    	ownerService.update(Database.SECONDARY, owner);
//    	assertEquals(0, cc.checkConsistency("Owners"));
    }
}
