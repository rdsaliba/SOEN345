package org.springframework.samples.petclinic.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Database access code for datasource routing example.
 */
public class OwnerDao {

    private static final String SQL_GET_TEST_OWNER_NAME = "select first_name from owners where last_name like 'test%'";

    private final JdbcTemplate jdbcTemplate;

    public OwnerDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    public String getOwnerName() {
        return this.jdbcTemplate.query(SQL_GET_TEST_OWNER_NAME, rowMapper).get(0);
    }

    private static RowMapper<String> rowMapper = (rs, rowNum) -> {
        return rs.getString("first_name");
    };
    
    public void update(int id, String firstName, String lastName, String address, String city, String telephone) {
        String query = "UPDATE owners SET first_name = ?, last_name = ?, address = ?, city = ?, telephone = ? WHERE id = ?";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query);
    	        preparedStmt.setString(1, firstName);
    	        preparedStmt.setString(2, lastName);
    	        preparedStmt.setString(3, address);
    	        preparedStmt.setString(4, city);
    	        preparedStmt.setString(5, telephone);
    	        preparedStmt.setInt(6, id);
				return preparedStmt;
    		}
    	};
    	this.jdbcTemplate.update(psc);
    }
    
    public int saveNew(String firstName, String lastName, String address, String city, String telephone) {
        String query = "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?)";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query, new String[] {"id"});
    	        preparedStmt.setString(1, firstName);
    	        preparedStmt.setString(2, lastName);
    	        preparedStmt.setString(3, address);
    	        preparedStmt.setString(4, city);
    	        preparedStmt.setString(5, telephone);
				return preparedStmt;
    		}
    	};
    	
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	this.jdbcTemplate.update(psc, keyHolder);
    	return keyHolder.getKey().intValue();
    }
}
