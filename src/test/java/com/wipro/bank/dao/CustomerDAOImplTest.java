package com.wipro.bank.dao;

import com.wipro.bank.bean.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class CustomerDAOImplTest {
    @Autowired
    private CustomerDAOImpl dao;

    @Autowired
    private JdbcTemplate template;

    private RowMapper<Integer> idMapper = (rs, num) -> rs.getInt("id");

    @Test
    public void saveCustomer_successfully() {
        CustomerDTO customer = new CustomerDTO("Peter Lee");
        customer = dao.saveCustomer(customer);
        assertNotNull(customer.getCustomerId());
    }

    @Test
    public void findOneById_thatDoesExist() {
        template.query("select id from customer_profile", idMapper)
                .forEach(id -> {
                    Optional<CustomerDTO> customer = dao.findById(id);
                    assertTrue(customer.isPresent());
                    assertEquals(id, customer.get().getCustomerId());
                });
    }

    @Test
    public void findOneById_thatDoesNotExist() {
        Optional<CustomerDTO> customer = dao.findById(999);
        assertFalse(customer.isPresent());
    }

    @Test
    public void findAll_successfully() {
        List<String> dbNames = dao.findAll().stream()
                                .map(CustomerDTO::getName)
                                .collect(Collectors.toList());
        assertThat(dbNames,
                containsInAnyOrder("Avinash Patel", "Jair Aviles", "Jagraj Singh", "Israel Eusebio"));
    }

    @Test
    public void count_successfully() {
        assertEquals(4, dao.count());
    }

    @Test
    public void delete_successfully() {
        template.query("select id from customer_profile", idMapper)
                .forEach(id ->{
                    Optional<CustomerDTO> customer = dao.findById(id);
                    assertTrue(customer.isPresent());
                    dao.delete(customer.get());
                });
        assertEquals(0, dao.count());
    }

    @Test
    public void existById() {
        template.query("select id from customer_profile", idMapper)
                .forEach(id -> assertTrue(dao.existsById(id)));
    }

    @Test
    public void doesNotExistsById() {
        List<Integer> ids = template.query("select id from customer_profile", idMapper);
        assertThat(ids, not(contains(999)));
        assertFalse(dao.existsById(999));
    }

}