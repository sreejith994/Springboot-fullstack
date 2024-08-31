package com.amigoscode.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;

    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper );
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id, name, email, age FROM customer
                WHERE id = ?
                """;

        List<Customer> res = jdbcTemplate.query(sql, new Object[]{id}, customerRowMapper);

        return res.stream().findFirst();
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {

        var sql = """
                SELECT * FROM customer WHERE email = ?
                """;

        List<Customer> res = jdbcTemplate.query(sql, new Object[]{email}, customerRowMapper);

        return res.stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?)
                """;
        int update = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
        System.out.println("jdbcTemplate.update: " + update);
    }

    @Override
    public void updateCustomer(Customer customer) {
        var sql = """
                UPDATE customer SET name=?, email=?, age=? WHERE id=?
                """;
        int update = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge(), customer.getId());
        System.out.println("jdbcTemplate.delete: " + update);
    }


    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE FROM customer WHERE id = ?
                """;
        int update = jdbcTemplate.update(sql, id);
        System.out.println("jdbcTemplate.delete: " + update);
    }
}
