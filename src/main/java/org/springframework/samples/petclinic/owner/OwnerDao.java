package org.springframework.samples.petclinic.owner;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
}
