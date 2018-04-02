package org.springframework.samples.petclinic.owner;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.samples.petclinic.database.Database;
import org.springframework.samples.petclinic.database.DatabaseThreadContext;
import org.springframework.samples.petclinic.visit.Visit;

/**
 * Database access code for datasource routing example.
 */
public class VisitDao {
    private final JdbcTemplate jdbcTemplate;

    public VisitDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }
    
    public List<Visit> findByPetId(int petId) {
        String query = "SELECT * FROM Visits WHERE pet_id = ?";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query);
    	        preparedStmt.setInt(1, petId);
				return preparedStmt;
    		}
    	};
    	List<Visit> visitList = this.jdbcTemplate.query(psc, new RowMapper<Visit>() {
	        public Visit mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Visit v = new Visit();
	            v.setId(rs.getInt(1));
	            v.setPetId(rs.getInt(2));
	            v.setDate(rs.getDate(3));
	            v.setDescription(rs.getString(4));
	            return v;
	        }
	    });
    	return visitList;
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
