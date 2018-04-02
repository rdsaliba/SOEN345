package org.springframework.samples.petclinic.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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
    
    public Collection<Owner> findByLastName(String lastName) {
        String query = "SELECT DISTINCT * FROM Owners owner left join pets ON owner.id = owner_id WHERE last_name LIKE ?";
        
    	PreparedStatementCreator psc = new PreparedStatementCreator() {
    		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    	        PreparedStatement preparedStmt = con.prepareStatement(query);
    	        preparedStmt.setString(1, lastName + "%");
				return preparedStmt;
    		}
    	};
    	List<Owner> ownerList = this.jdbcTemplate.query(psc, new RowMapper<Owner>() {
	        public Owner mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Owner o = new Owner();
	            o.setId(rs.getInt(1));
	            o.setFirstName(rs.getString(2));
	            o.setLastName(rs.getString(3));
	            o.setAddress(rs.getString(4));
	            o.setCity(rs.getString(5));
	            o.setTelephone(rs.getString(6));
	            return o;
	        }
	    });
    	return ownerList;
    }
    
    public Owner findById(int id) {
        String query = "SELECT * FROM Owners owner left join pets ON owner.id = owner_id WHERE owner.id = ?";
    	
    	Owner owner = this.jdbcTemplate.queryForObject(query, new Object[] { id }, new RowMapper<Owner>() {
	    	public Owner mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Owner o = new Owner();
	            o.setId(rs.getInt(1));
	            o.setFirstName(rs.getString(2));
	            o.setLastName(rs.getString(3));
	            o.setAddress(rs.getString(4));
	            o.setCity(rs.getString(5));
	            o.setTelephone(rs.getString(6));
	            return o;
	        }
    	});
    	
    	return owner; 
    }
    
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
