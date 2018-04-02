package org.springframework.samples.petclinic.owner;

import java.util.Collection;
import java.util.List;

import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;

/**
 * Service layer code for datasource routing example.  Here, the service methods are responsible
 * for setting and clearing the context.
 */
public class PetService {

    private final PetDao petDao;

    public PetService(PetDao petDao) {
        this.petDao = petDao;
    }
    
    public List<PetType>findPetTypes(Database db) {
        DatabaseThreadContext.setCurrentDatabase(db);
        List<PetType> petTypes = this.petDao.findPetTypes();
        DatabaseThreadContext.clearCurrentDatabase();
        return petTypes;
    }
    
    public Pet findById(Database db, int id) {
        DatabaseThreadContext.setCurrentDatabase(db);
        Pet pet = this.petDao.findById(id);
        DatabaseThreadContext.clearCurrentDatabase();
        return pet;
    }
    
    public void update(Database db, Pet pet) {
        DatabaseThreadContext.setCurrentDatabase(db);
        this.petDao.update(pet.getId(), pet.getName(), pet.getBirthDate(), pet.getType().getId(), pet.getOwner().getId());
        DatabaseThreadContext.clearCurrentDatabase();
    }
    
    public int saveNew(Database db, Pet pet) {
        DatabaseThreadContext.setCurrentDatabase(db);
        int id = this.petDao.saveNew(pet.getName(), pet.getBirthDate(), pet.getType().getId(), pet.getOwner().getId());
        DatabaseThreadContext.clearCurrentDatabase();
        return id;
    }
}