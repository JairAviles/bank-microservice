package com.wipro.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.bean.CustomerRequest;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ExtendWith(SpringExtension.class)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @MockBean
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<CustomerDTO> customers;

    @BeforeEach
    public void setUp() {
        customers = new ArrayList<>(Arrays.asList(
                CustomerDTO.builder().customerId(1).name("Jair Aviles").build(),
                CustomerDTO.builder().customerId(2).name("Israel Eusebio").build(),
                CustomerDTO.builder().customerId(3).name("Avinash Patel").build(),
                CustomerDTO.builder().customerId(4).name("Jagraj Singh").build()
        ));
    }

    @AfterEach
    public void tearDown() {
        customers.clear();
    }

    @Test
    public void givenCustomers_whenCreateCustomer_thenReturnJsonObject() throws Exception {
        CustomerDTO newCustomer = CustomerDTO.builder().customerId(5).name("Marty McCustomer").build();
        CustomerRequest customerRequest = CustomerRequest.builder().name("Marty McCustomer").build();
        String json = objectMapper.writeValueAsString(customerRequest);
        given(service.addCustomer(any())).willReturn(newCustomer);
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Marty McCustomer"))
                .andReturn();
    }

    @Test
    public void givenCustomers_whenUpdateCustomer_thenReturnJsonObject() throws Exception {
        CustomerDTO updatedCustomer = CustomerDTO.builder().customerId(1).name("Marty McCustomer").build();
        CustomerRequest customerRequest = CustomerRequest.builder().name("Marty McCustomer").build();
        String json = objectMapper.writeValueAsString(customerRequest);
        given(service.getCustomer(anyInt())).willReturn(Optional.of(customers.get(0)));
        given(service.updateCustomer(any())).willReturn(updatedCustomer);
        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name").value("Marty McCustomer"))
                .andReturn();
    }

    @Test
    public void givenCustomers_whenUpdateCustomer_thenReturnNotFoundStatus() throws Exception {
        CustomerRequest customerRequest = CustomerRequest.builder().name("Marty McCustomer").build();
        String json = objectMapper.writeValueAsString(customerRequest);
        given(service.getCustomer(anyInt())).willReturn(Optional.empty());
        mockMvc.perform(put("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("utf-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCustomers_whenFindAll__thenReturnJsonObject() throws Exception {
        given(service.getAllCustomers()).willReturn(customers);

        mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    public void givenCustomer_whenFindById_thenReturnJsonObject() throws Exception {
        given(service.getCustomer(anyInt())).willReturn(Optional.of(customers.get(0)));

        mockMvc.perform(get("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.name").value("Jair Aviles"));
    }

    @Test
    public void givenCustomer_whenNotFindById_thenReturnNotFoundStatus() throws Exception {
        given(service.getCustomer(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCustomer_whenRemoveCustomer_thenReturnNoContentStatus() throws Exception {
        given(service.getCustomer(anyInt())).willReturn(Optional.of(customers.get(0)));

        mockMvc.perform(delete("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenCustomer_whenRemoveCustomerNotFound_thenReturnNotFoundStatus() throws Exception {
        given(service.getCustomer(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}