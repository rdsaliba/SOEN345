package org.springframework.samples.petclinic.vet;

import java.util.Collection;

import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;

public class VetService {
    private final VetDao vetDao;

    public VetService(VetDao vetDao) {
        this.vetDao = vetDao;
    }
    
    public Collection<Vet> findAll(Database db) {
        DatabaseThreadContext.setCurrentDatabase(db);
        Collection<Vet> vets = this.vetDao.findAll();
        DatabaseThreadContext.clearCurrentDatabase();
        return vets;
    }
}
