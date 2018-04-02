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
public class PetDao {
    private final JdbcTemplate jdbcTemplate;

    public PetDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }
    
    public void update(int id, String name, Date birthDate, int type, int owner) {
        String query = "UPDATE pets SET name = ?, birth_date = ?, type_id = ?, owner_id = ? WHERE id = ?";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query);
    	        preparedStmt.setString(1, name);
    	        preparedStmt.setDate(2, new java.sql.Date(birthDate.getTime()));
    	        preparedStmt.setInt(3, type);
    	        preparedStmt.setInt(4, owner);
    	        preparedStmt.setInt(5, id);
				return preparedStmt;
    		}
    	};
    	this.jdbcTemplate.update(psc);
    }
    
    public int saveNew(String name, Date birthDate, int type, int owner) {
        String query = "INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES (?, ?, ?, ?)";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query, new String[] {"id"});
    	        preparedStmt.setString(1, name);
    	        preparedStmt.setDate(2, new java.sql.Date(birthDate.getTime()));
    	        preparedStmt.setInt(3, type);
    	        preparedStmt.setInt(4, owner);
				return preparedStmt;
    		}
    	};
    	
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	this.jdbcTemplate.update(psc, keyHolder);
    	return keyHolder.getKey().intValue();
    }
}
