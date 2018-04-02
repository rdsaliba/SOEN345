package org.springframework.samples.petclinic.owner;

import java.util.Collection;

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
    
    public Collection<Owner> findByLastName(Database db, String lastName) {
        DatabaseThreadContext.setCurrentDatabase(db);
        Collection<Owner> owners = this.ownerDao.findByLastName(lastName);
        DatabaseThreadContext.clearCurrentDatabase();
        return owners;
    }
    
    public Owner findById(Database db, int id) {
        DatabaseThreadContext.setCurrentDatabase(db);
        Owner owner = this.ownerDao.findById(id);
        DatabaseThreadContext.clearCurrentDatabase();
        return owner;
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