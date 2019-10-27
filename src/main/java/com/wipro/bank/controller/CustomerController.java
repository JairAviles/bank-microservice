package com.wipro.bank.controller;

import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.bean.CustomerRequest;
import com.wipro.bank.exception.CustomerNotFoundException;
import com.wipro.bank.exception.BadRequestException;
import com.wipro.bank.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log
@RestController
@RequestMapping("/api/customers")
@Api(tags = "customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = "application/json")
    @ApiOperation(value = "List Customers", notes = "Service for finding all existing customers")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Customers found")})
    public ResponseEntity<List<CustomerDTO>> findAll() {
        return ResponseEntity.ok(this.service.getAllCustomers());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Find Customer by Id", notes = "Service for finding an existing customer by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Customer found"),
            @ApiResponse(code = 404, message = "Customer not found")
    })
    public ResponseEntity<CustomerDTO> findById(@PathVariable("id") Integer customerId) {
        Optional<CustomerDTO> customer = this.service.getCustomer(customerId);

        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            String errorMessage = String.format("Customer with id: %d not found", customerId);
            log.severe(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        }
    }

    @PostMapping(produces = "application/json")
    @ApiOperation(value = "Create Customer", notes = "Service for creating a new customer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Customer created successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerRequest customer) {
        CustomerDTO customerDto = convertToDto(customer);
        return new ResponseEntity<>(this.service.addCustomer(customerDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Update Customer", notes = "Service for updating an existing customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Customer updated successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") Integer customerId, @RequestBody CustomerRequest customer) {
        Optional<CustomerDTO> customerDto = this.service.getCustomer(customerId);
        if (!customerDto.isPresent()) {
            String errorMessage = String.format("Customer with id: %d not found", customerId);
            log.severe(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        } else {
            customer.setCustomerId(customerId);
            CustomerDTO updatedCustomerDto = convertToDto(customer);
            return new ResponseEntity<>(this.service.updateCustomer(updatedCustomerDto), HttpStatus.OK);
        }

    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Customer by Id", notes = "Service for finding an existing customer by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Customer deleted successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Customer not found")
    })
    public ResponseEntity<String> removeCustomer(@PathVariable("id") Integer customerId) {
        Optional<CustomerDTO> customer = this.service.getCustomer(customerId);

        if (!customer.isPresent()) {
            String errorMessage = String.format("Customer with id: %d not found", customerId);
            log.severe(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        } else {
            this.service.removeCustomer(customer.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    private CustomerDTO convertToDto(CustomerRequest customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            String errorMessage = String.format("Customer name is required for this operation");
            log.severe(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        CustomerDTO customerDto = modelMapper.map(customer, CustomerDTO.class);
        return customerDto;
    }
}
