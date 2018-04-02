package org.springframework.samples.petclinic.owner;

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