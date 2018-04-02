package org.springframework.samples.petclinic.vet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class VetDao {
	private final JdbcTemplate jdbcTemplate;

    public VetDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }
    
    public Collection<Vet> findAll() {
        String query = "SELECT * FROM Vets";
        
    	List<Vet> vetsList = this.jdbcTemplate.query(query, new RowMapper<Vet>() {
	        public Vet mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Vet v = new Vet();
	            v.setId(rs.getInt(1));
	            v.setFirstName(rs.getString(2));
	            v.setLastName(rs.getString(3));
	            return v;
	        }
	    });
    	return vetsList;
    }

}
