package com.wipro.bank.dao;

import com.wipro.bank.bean.AccountDTO;
import com.wipro.bank.bean.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve"})
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class AccountDAOImplTest {
    @Autowired
    private AccountDAOImpl dao;

    @Autowired
    private JdbcTemplate template;

    private List<AccountDTO> accounts;

    @BeforeEach
    public void setUp() {
        accounts = Arrays.asList(
                new AccountDTO(new CustomerDTO("Jair Aviles"), 1234.79),
                new AccountDTO(new CustomerDTO("Jair Aviles"), 5000.0),
                new AccountDTO(new CustomerDTO("Avinash Patel"), 10542.84)
        );
        accounts.stream().forEach(account -> dao.saveAccount(account));
    }


    private RowMapper<Integer> idMapper = (rs, num) -> rs.getInt("id");

    @Test
    public void saveAccount_successfully() {
        AccountDTO account = new AccountDTO(new CustomerDTO("Jagraj Singh"), 2000.43);
        account = dao.saveAccount(account);
        assertNotNull(account.getAccountId());
    }

    @Test
    public void findOneById_thatDoesExist() {
        template.query("select id from account", idMapper)
                .forEach(id -> {
                    Optional<AccountDTO> customer = dao.findById(id);
                    assertTrue(customer.isPresent());
                    assertEquals(id, customer.get().getAccountId());
                });
    }

    @Test
    public void findOneById_thatDoesNotExist() {
        Optional<AccountDTO> customer = dao.findById(999);
        assertFalse(customer.isPresent());
    }

    @Test
    public void findAll_successfully() {
        List<Integer> dbAccounts = dao.findAll().stream()
                .map(AccountDTO::getAccountId)
                .collect(Collectors.toList());
        assertTrue(dbAccounts.size() > 0);
    }

    @Test
    public void findByCustomerId_thatDoesNotExist() {
        List<AccountDTO> accounts = dao.findByCustomerId(999);
        assertTrue(accounts.size() == 0);
    }

    @Test
    public void count_successfully() {
        assertEquals(3, dao.count());
    }

    @Test
    public void delete_successfully() {
        template.query("select id from account", idMapper)
                .forEach(id ->{
                    Optional<AccountDTO> account = dao.findById(id);
                    assertTrue(account.isPresent());
                    dao.delete(account.get());
                });
        assertEquals(0, dao.count());
    }

}