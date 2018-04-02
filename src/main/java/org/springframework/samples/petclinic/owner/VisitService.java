package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;
import org.springframework.samples.petclinic.visit.Visit;

/**
 * Service layer code for datasource routing example.  Here, the service methods are responsible
 * for setting and clearing the context.
 */
public class VisitService {

    private final VisitDao visitDao;

    public VisitService(VisitDao visitDao) {
        this.visitDao = visitDao;
    }
    
    public void update(Database db, Visit visit) {
        DatabaseThreadContext.setCurrentDatabase(db);
        this.visitDao.update(visit.getId(), visit.getPetId(), visit.getDate(), visit.getDescription());
        DatabaseThreadContext.clearCurrentDatabase();
    }
    
    public int saveNew(Database db, Visit visit) {
        DatabaseThreadContext.setCurrentDatabase(db);
        int id = this.visitDao.saveNew(visit.getPetId(), visit.getDate(), visit.getDescription());
        DatabaseThreadContext.clearCurrentDatabase();
        return id;
    }
}