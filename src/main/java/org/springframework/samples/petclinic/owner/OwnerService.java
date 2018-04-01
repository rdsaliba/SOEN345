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
}