package com.wipro.bank.service;

import com.wipro.bank.bean.AccountDTO;
import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.dao.IAccountDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AccountServiceWithMocksTest {

    @InjectMocks
    private AccountService service;

    @Mock
    @Qualifier("accountDAOImpl")
    private IAccountDAO dao;

    private List<AccountDTO> accounts;
    private List<CustomerDTO> customers;

    @BeforeEach
    public void setUp() {
        customers = new ArrayList<>(Arrays.asList(
                CustomerDTO.builder().customerId(1).name("Jair Aviles").build(),
                CustomerDTO.builder().customerId(2).name("Israel Eusebio").build(),
                CustomerDTO.builder().customerId(3).name("Avinash Patel").build(),
                CustomerDTO.builder().customerId(4).name("Jagraj Singh").build()
        ));
        accounts = new ArrayList<>(Arrays.asList(
                AccountDTO.builder().accountId(1).balance(2043.27).customer(customers.get(1)).build(),
                AccountDTO.builder().accountId(2).balance(3509.33).customer(customers.get(1)).build(),
                AccountDTO.builder().accountId(3).balance(10000.0).customer(customers.get(3)).build()
        ));
    }

    @AfterEach
    public void tearDown() {
        accounts.clear();
        customers.clear();
    }

    @Test
    public void addAccount_successfully() {
        AccountDTO newAccount = AccountDTO.builder().accountId(4).balance(543.21).customer(customers.get(2)).build();
        when(service.addAccount(any())).thenReturn(newAccount);
        AccountDTO addedAccount = service.addAccount(newAccount);
        assertThat(addedAccount, not(nullValue()));
        assertThat(addedAccount.getAccountId(), is(greaterThan(3)));
        assertThat(addedAccount.getBalance(), is(543.21));
        verify(dao, atMostOnce()).saveAccount(any());
    }

    @Test
    public void updateAccount_successfully() {
        AccountDTO existingAccount = AccountDTO.builder().accountId(1).balance(0.00).customer(customers.get(1)).build();
        when(service.updateAccount(any())).thenReturn(existingAccount);
        AccountDTO updatedAccount = service.updateAccount(existingAccount);
        assertThat(updatedAccount, not(nullValue()));
        assertThat(updatedAccount.getAccountId(), is(1));
        assertThat(updatedAccount.getBalance(), is(0.00));
        verify(dao, atMostOnce()).saveAccount(any());
    }

    @Test
    public void removeAccount_successfully() {
        accounts.stream().forEach(account -> service.removeAccount(account));
        verify(dao, atLeast(accounts.size())).delete(any());
    }

    @Test
    public void getBalanceOf_thatDoesExist() {
        when(service.getBalanceOf(anyInt())).thenReturn(Optional.of(accounts.get(anyInt())));
        accounts.stream().forEach(account -> {
            Optional<AccountDTO> accountDto = service.getBalanceOf(account.getAccountId());
            assertTrue(accountDto.isPresent());
        });
        verify(dao, atLeast(accounts.size())).findById(anyInt());
    }

    @Test
    public void getCustomer_thatDoesNotExist() {
        when(service.getBalanceOf(anyInt())).thenReturn(Optional.empty());
        Optional<AccountDTO> accountDto = service.getBalanceOf(999);
        assertFalse(accountDto.isPresent());
        verify(dao, atMostOnce()).findById(anyInt());
    }

    @Test
    public void getAllAccounts_successfully() {
        when(service.getAllAccounts()).thenReturn(accounts);
        List<AccountDTO> accountDtos = service.getAllAccounts();
        assertThat(accountDtos, not(emptyIterable()));
        verify(dao, atMostOnce()).findAll();
    }

    @Test
    public void transferFunds_notExistingAccount() {
        when(service.exists(1)).thenReturn(true);
        when(service.exists(999)).thenReturn(false);
        String transferResult = service.transferFunds(1, 999, 1000.00);
        assertEquals(transferResult, TransferStatus.ID_MISMATCH.toString());
        verify(dao, times(2)).existsById(anyInt());
    }

    @Test
    public void transferFunds_notSufficientFunds() {
        when(service.exists(1)).thenReturn(true);
        when(service.exists(2)).thenReturn(true);
        when(service.getBalanceOf(1)).thenReturn(Optional.of(accounts.get(1)));
        when(service.getBalanceOf(2)).thenReturn(Optional.of(accounts.get(2)));
        String transferResult = service.transferFunds(1, 2, 9000.00);
        assertEquals(transferResult, TransferStatus.INSUFFICIENT_FUNDS.toString());
        verify(dao, times(2)).existsById(anyInt());
        verify(dao, times(2)).findById(anyInt());
    }

    @Test
    public void transferFunds_successfully() {
        when(service.exists(1)).thenReturn(true);
        when(service.exists(2)).thenReturn(true);
        when(service.getBalanceOf(1)).thenReturn(Optional.of(accounts.get(1)));
        when(service.getBalanceOf(2)).thenReturn(Optional.of(accounts.get(2)));
        String transferResult = service.transferFunds(1, 2, 500.00);
        assertEquals(transferResult, TransferStatus.SUCCESS.toString());
        verify(dao, times(2)).existsById(anyInt());
        verify(dao, times(2)).findById(anyInt());
        verify(dao, times(2)).saveAccount(any());
    }

}