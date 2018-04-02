package org.springframework.samples.petclinic.system;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.samples.petclinic.database.ConsistencyChecker;
import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.VisitService;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetService;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class MigrationTests {

    private static final int TEST_OWNER_ID = 100;
    
    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private VisitService visitService;
    
    @Autowired
    private VetService vetService;

    private Owner owner;
    private Pet pet;
    private Visit visit;
    private ConsistencyChecker cc;
    
    @Before
    public void setup() {
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
        
        pet = new Pet();
        pet.setId(123);
        pet.setName("Bob");
        pet.setBirthDate(new Date());
        pet.setType(cat);
        owner.addPet(pet);
        
        visit = new Visit();
        visit.setId(100);
        visit.setDate(new Date());
        visit.setPetId(1);
        visit.setDescription("description");
    }
    
    @Test
    @Transactional
    public void testConsistency() throws Exception {
    	/// OWNER ///
    	//Checks if new and old database insert owner correctly
    	int id = ownerService.saveNew(Database.PRIMARY, owner);
    	int id2 = ownerService.saveNew(Database.SECONDARY, owner);
    	cc = new ConsistencyChecker("Owners");
    	assertEquals(0, cc.checkConsistency("Owners"));
    	
    	//Checks if inconsistent writes are fixed for owner
    	ownerService.saveNew(Database.PRIMARY, owner);
    	cc = new ConsistencyChecker("Owners");
    	assertEquals(1, cc.checkConsistency("Owners"));
    	assertEquals(0, cc.checkConsistency("Owners"));
    	
    	//Checks if new and old database update owner correctly
    	owner.setId(id);
    	owner.setAddress("new address");
    	ownerService.update(Database.PRIMARY, owner);
    	ownerService.update(Database.SECONDARY, owner);
    	cc = new ConsistencyChecker("Owners");
    	assertEquals(0, cc.checkConsistency("Owners"));
    	
    	//Checks if reads are consistent in owner
    	Owner o1 = ownerService.findById(Database.PRIMARY, id);
    	Owner o2 = ownerService.findById(Database.SECONDARY, id);
    	assertEquals(o1, o2);
    	Collection<Owner> o3 = ownerService.findByLastName(Database.PRIMARY, "test");
    	Collection<Owner> o4 = ownerService.findByLastName(Database.PRIMARY, "test");
    	assertEquals(o3, o4);
    	
    	/// PETS ///
    	//Checks if new and old database insert pet correctly
    	id = petService.saveNew(Database.PRIMARY, pet);
    	id2 = petService.saveNew(Database.SECONDARY, pet);
    	cc = new ConsistencyChecker("Pets");
    	assertEquals(0, cc.checkConsistency("Owners"));
    	
    	//Checks if inconsistent writes are fixed for pet
    	petService.saveNew(Database.PRIMARY, pet);
    	cc = new ConsistencyChecker("Pets");
    	assertEquals(1, cc.checkConsistency("Pets"));
    	assertEquals(0, cc.checkConsistency("Pets"));
    	
    	//Checks if new and old database update pet correctly
    	pet.setId(id);
    	pet.setName("new name");
    	petService.update(Database.PRIMARY, pet);
    	petService.update(Database.SECONDARY, pet);
    	cc = new ConsistencyChecker("Pets");
    	assertEquals(0, cc.checkConsistency("Pets"));
    	
    	//Checks if reads are consistent in pets
    	Pet p1 = petService.findById(Database.PRIMARY, id);
    	Pet p2 = petService.findById(Database.SECONDARY, id);
    	assertEquals(p1, p2);
    	Collection<PetType> p3 = petService.findPetTypes(Database.PRIMARY);
    	Collection<PetType> p4 = petService.findPetTypes(Database.SECONDARY);
    	assertEquals(p3, p4);
    	
    	/// VISITS ///
    	//Checks if new and old database insert visit correctly
    	id = visitService.saveNew(Database.PRIMARY, visit);
    	id2 = visitService.saveNew(Database.SECONDARY, visit);
    	cc = new ConsistencyChecker("Visits");
    	assertEquals(0, cc.checkConsistency("Visits"));
    	
    	//Checks if inconsistent writes are fixed for visit
    	visitService.saveNew(Database.PRIMARY, visit);
    	cc = new ConsistencyChecker("Visits");
    	assertEquals(1, cc.checkConsistency("Visits"));
    	assertEquals(0, cc.checkConsistency("Visits"));
    	
    	//Checks if new and old database update visit correctly
    	visit.setId(id);
    	visit.setDescription("new description");
    	visitService.update(Database.PRIMARY, visit);
    	visitService.update(Database.SECONDARY, visit);
    	cc = new ConsistencyChecker("Visits");
    	assertEquals(0, cc.checkConsistency("Visits"));
    	
    	//Checks if reads are consistent in visit
    	Collection<Visit> v1 = visitService.findByPetId(Database.PRIMARY, 8);
    	Collection<Visit> v2 = visitService.findByPetId(Database.SECONDARY, 8);
    	assertEquals(v1, v2);
    	
    	
    	/// VETS ///
    	//Checks if reads are consistent in vet
    	Collection<Vet> v3 = vetService.findAll(Database.PRIMARY);
    	Collection<Vet> v4 = vetService.findAll(Database.SECONDARY);
    	assertEquals(v3, v4);
    }
}
