package com.wipro.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.bank.bean.AccountDTO;
import com.wipro.bank.bean.AccountRequest;
import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.service.AccountService;
import com.wipro.bank.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@ExtendWith(SpringExtension.class)
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper = new ObjectMapper();
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
    public void givenAccounts_whenCreateAccount_thenReturnJsonObject() throws Exception {
        AccountDTO newAccount = AccountDTO.builder().accountId(1).balance(900.00).customer(customers.get(1)).build();
        AccountRequest accountRequest = AccountRequest.builder().balance(900.00).customerId(1).build();
        String json = objectMapper.writeValueAsString(accountRequest);
        given(customerService.getCustomer(anyInt())).willReturn(Optional.of(customers.get(0)));
        given(modelMapper.map(any(), any())).willReturn(newAccount);
        given(accountService.addAccount(any())).willReturn(newAccount);
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.balance").value(900.00))
                .andReturn();
    }

    @Test
    public void givenAccounts_whenCreateAccount_thenBadRequestStatus() throws Exception {
        AccountDTO newAccount = AccountDTO.builder().accountId(1).balance(900.00).customer(customers.get(1)).build();
        AccountRequest accountRequest = AccountRequest.builder().customerId(1).build();
        String json = objectMapper.writeValueAsString(accountRequest);
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void givenAccounts_whenCreateAccount_thenNotFoundStatus() throws Exception {
        AccountDTO newAccount = AccountDTO.builder().accountId(1).balance(900.00).customer(customers.get(1)).build();
        AccountRequest accountRequest = AccountRequest.builder().balance(900.00).customerId(999).build();
        String json = objectMapper.writeValueAsString(accountRequest);
        given(customerService.getCustomer(anyInt())).willReturn(Optional.empty());
        given(modelMapper.map(any(), any())).willReturn(newAccount);
        given(accountService.addAccount(any())).willReturn(newAccount);
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenAccounts_whenUpdateAccountThatDoesExist_thenReturnJsonObject() throws Exception {
        AccountDTO updatedAccount = AccountDTO.builder().accountId(3).balance(2314.27).customer(customers.get(3)).build();
        AccountRequest accountRequest = AccountRequest.builder().balance(2314.27).customerId(3).build();
        String json = objectMapper.writeValueAsString(accountRequest);
        given(accountService.getBalanceOf(any())).willReturn(Optional.of(accounts.get(2)));
        given(customerService.getCustomer(anyInt())).willReturn(Optional.of(customers.get(3)));
        given(modelMapper.map(any(), any())).willReturn(updatedAccount);
        given(accountService.updateAccount(any())).willReturn(updatedAccount);
        mockMvc.perform(put("/api/accounts/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(3))
                .andExpect(jsonPath("$.balance").value(2314.27))
                .andReturn();
    }

    @Test
    public void givenAccounts_whenUpdateAccountThatDoesNotExist_thenNotFound() throws Exception {
        AccountRequest accountRequest = AccountRequest.builder().balance(2314.27).customerId(3).build();
        String json = objectMapper.writeValueAsString(accountRequest);
        given(accountService.getBalanceOf(any())).willReturn(Optional.empty());
        mockMvc.perform(put("/api/accounts/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenAccounts_whenUpdateAccountWhenCustomerNotFound_thenNotFound() throws Exception {
        AccountDTO updatedAccount = AccountDTO.builder().accountId(3).balance(2314.27).customer(customers.get(3)).build();
        AccountRequest accountRequest = AccountRequest.builder().balance(10000.00).customerId(999).build();
        String json = objectMapper.writeValueAsString(accountRequest);
        given(accountService.getBalanceOf(any())).willReturn(Optional.of(accounts.get(2)));
        given(customerService.getCustomer(anyInt())).willReturn(Optional.empty());
        mockMvc.perform(put("/api/accounts/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenAccounts_whenFindAll__thenReturnJsonObject() throws Exception {
        given(accountService.getAllAccounts()).willReturn(accounts);

        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    public void givenAccount_whenFindById_thenReturnJsonObject() throws Exception {
        given(accountService.getBalanceOf(anyInt())).willReturn(Optional.of(accounts.get(0)));

        mockMvc.perform(get("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.balance").value(2043.27))
                .andExpect(jsonPath("$.customer").doesNotExist());
    }

    @Test
    public void givenAccount_whenNotFindById_thenReturnNotFoundStatus() throws Exception {
        given(accountService.getBalanceOf(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenAccount_whenRemoveCustomer_thenReturnNoContentStatus() throws Exception {
        given(accountService.getBalanceOf(anyInt())).willReturn(Optional.of(accounts.get(0)));

        mockMvc.perform(delete("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenAccount_whenRemoveCustomerNotFound_thenReturnNotFoundStatus() throws Exception {
        given(accountService.getBalanceOf(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/accounts/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}