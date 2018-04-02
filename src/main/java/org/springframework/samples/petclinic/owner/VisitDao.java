package org.springframework.samples.petclinic.owner;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Database access code for datasource routing example.
 */
public class VisitDao {
    private final JdbcTemplate jdbcTemplate;

    public VisitDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }
    
    public void update(int id, int petId, Date visitDate, String desc) {
        String query = "UPDATE visits SET pet_id = ?, visit_date = ?, description = ? WHERE id = ?";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query);
    	        preparedStmt.setInt(1, petId);
    	        preparedStmt.setDate(2, new java.sql.Date(visitDate.getTime()));
    	        preparedStmt.setString(3, desc);
    	        preparedStmt.setInt(4, id);
				return preparedStmt;
    		}
    	};
    	this.jdbcTemplate.update(psc);
    }
    
    public int saveNew(int petId, Date visitDate, String desc) {
        String query = "INSERT INTO visits (pet_id, visit_date, description) VALUES (?, ?, ?)";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query, new String[] {"id"});
    	        preparedStmt.setInt(1, petId);
    	        preparedStmt.setDate(2, new java.sql.Date(visitDate.getTime()));
    	        preparedStmt.setString(3, desc);
				return preparedStmt;
    		}
    	};
    	
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	this.jdbcTemplate.update(psc, keyHolder);
    	return keyHolder.getKey().intValue();
    }
}
