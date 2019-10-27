package com.wipro.bank.service;

import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.dao.ICustomerDAO;
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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CustomerServiceWithMocksTest {
    @InjectMocks
    private CustomerService service;

    @Mock
    @Qualifier("customerDAOImpl")
    private ICustomerDAO dao;

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
    public void addCustomer_successfully() {
        when(service.addCustomer(any())).thenReturn(CustomerDTO.builder().customerId(5).name("Marty McCustomer").build());
        CustomerDTO newCustomer = service.addCustomer(CustomerDTO.builder().name("Marty McCustomer").build());
        assertThat(newCustomer, not(nullValue()));
        assertThat(newCustomer.getCustomerId(), is(greaterThan(4)));
        assertThat(newCustomer.getName(), notNullValue());
        verify(dao, atMostOnce()).saveCustomer(any());
    }

    @Test
    public void updateCustomer_successfully() {
        when(service.updateCustomer(any())).thenReturn(CustomerDTO.builder().customerId(1).name("New Customer Updated").build());
        CustomerDTO newCustomer = service.updateCustomer(CustomerDTO.builder().customerId(1).name("New Customer Updated").build());
        assertThat(newCustomer, not(nullValue()));
        assertThat(newCustomer.getCustomerId(), is(1));
        assertThat(newCustomer.getName(), is("New Customer Updated"));
        verify(dao, atMostOnce()).saveCustomer(any());
    }

    @Test
    public void removeCustomer_successfully() {
        customers.stream().forEach(customer -> service.removeCustomer(customer));
        verify(dao, atLeast(customers.size())).delete(any());
    }

    @Test
    public void getCustomer_thatDoesExist() {
        when(service.getCustomer(anyInt())).thenReturn(Optional.of(customers.get(anyInt())));
        customers.stream().forEach(customer -> {
            Optional<CustomerDTO> customerDto = service.getCustomer(customer.getCustomerId());
            assertTrue(customerDto.isPresent());
        });
        verify(dao, atLeast(customers.size())).findById(anyInt());
    }

    @Test
    public void getCustomer_thatDoesNotExist() {
        when(service.getCustomer(anyInt())).thenReturn(Optional.empty());
        Optional<CustomerDTO> customerDto = service.getCustomer(999);
        assertFalse(customerDto.isPresent());
        verify(dao, atMostOnce()).findById(anyInt());
    }

    @Test
    public void getAllCustomers_successfully() {
        when(service.getAllCustomers()).thenReturn(customers);
        List<CustomerDTO> customersDto = service.getAllCustomers();
        assertThat(customersDto, not(emptyIterable()));
        verify(dao, atMostOnce()).findAll();
    }

    @Test
    public void customer_doesExist() {
        when(service.exists(anyInt())).thenReturn(true);
        customers.stream().forEach(customer -> {
            boolean customerExists = service.exists(customer.getCustomerId());
            assertTrue(customerExists);
        });
        verify(dao, atLeast(customers.size())).existsById(anyInt());
    }

    @Test
    public void customer_doesNotExist() {
        when(service.exists(anyInt())).thenReturn(false);
        boolean customerExists = service.exists(999);
        assertFalse(customerExists);
        verify(dao, atMostOnce()).existsById(anyInt());
    }
}