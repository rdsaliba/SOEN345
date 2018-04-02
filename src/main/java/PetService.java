package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;

/**
 * Service layer code for datasource routing example.  Here, the service methods are responsible
 * for setting and clearing the context.
 */
public class OwnerService {

    private final OwnerDao ownerDao;

    public OwnerService(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    public String getOwnerName(Database db) {
        DatabaseThreadContext.setCurrentDatabase(db);
        String ownerName = this.ownerDao.getOwnerName();
        DatabaseThreadContext.clearCurrentDatabase();
        return ownerName;
    }
    
    public void update(Database db, Owner owner) {
        DatabaseThreadContext.setCurrentDatabase(db);
        this.ownerDao.update(owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(), owner.getTelephone());
        DatabaseThreadContext.clearCurrentDatabase();
    }
    
    public int saveNew(Database db, Owner owner) {
        DatabaseThreadContext.setCurrentDatabase(db);
        int id = this.ownerDao.saveNew(owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(), owner.getTelephone());
        DatabaseThreadContext.clearCurrentDatabase();
        return id;
    }
}