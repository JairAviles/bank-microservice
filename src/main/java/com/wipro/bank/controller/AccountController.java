package com.wipro.bank.controller;

import com.wipro.bank.bean.AccountDTO;
import com.wipro.bank.bean.AccountRequest;
import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.exception.AccountNotFoundException;
import com.wipro.bank.exception.BadRequestException;
import com.wipro.bank.exception.CustomerNotFoundException;
import com.wipro.bank.service.AccountService;
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
@RequestMapping("/api/accounts")
@Api(tags = "accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(produces = "application/json")
    @ApiOperation(value = "Create Account", notes = "Service for creating a new customer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Account created successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Customer not found")
    })
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountRequest account) {
        AccountDTO accountDto = convertToDto(account);
        return new ResponseEntity<>(this.service.addAccount(accountDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Update Account", notes = "Service for updating an existing account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account updated successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable("id") Integer accountId, @RequestBody AccountRequest account) {
        Optional<AccountDTO> accountDto = this.service.getBalanceOf(accountId);
        if (!accountDto.isPresent()) {
            String errorMessage = String.format("Account with id: %d not found", accountId);
            log.severe(errorMessage);
            throw new AccountNotFoundException(errorMessage);
        } else {
            account.setCustomerId(accountId);
            AccountDTO updatedAccountDto = convertToDto(account);
            return new ResponseEntity<>(this.service.updateAccount(updatedAccountDto), HttpStatus.OK);
        }

    }

    @GetMapping(produces = "application/json")
    @ApiOperation(value = "List Accounts", notes = "Service for finding all existing accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Accounts found")})
    public ResponseEntity<List<AccountDTO>> findAll() {
        return ResponseEntity.ok(this.service.getAllAccounts());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Find Account by Id", notes = "Service for finding an existing account by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account found"),
            @ApiResponse(code = 404, message = "Account not found")
    })
    public ResponseEntity<AccountDTO> findById(@PathVariable("id") Integer accountId) {
        Optional<AccountDTO> account = this.service.getBalanceOf(accountId);

        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            String errorMessage = String.format("Account with id: %d not found", accountId);
            log.severe(errorMessage);
            throw new AccountNotFoundException(errorMessage);
        }
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value = "Account by Id", notes = "Service for finding an existing account by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Customer deleted successfully"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Customer not found")
    })
    public ResponseEntity<String> removeAccount(@PathVariable("id") Integer accountId) {
        Optional<AccountDTO> account = this.service.getBalanceOf(accountId);

        if (!account.isPresent()) {
            String errorMessage = String.format("Account with id: %d not found", accountId);
            log.severe(errorMessage);
            throw new AccountNotFoundException(errorMessage);
        } else {
            this.service.removeAccount(account.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    private AccountDTO convertToDto(AccountRequest account) {
        if (account.getBalance() == null || account.getBalance() < 0) {
            String errorMessage = String.format("Balance amount is invalid");
            log.severe(errorMessage);
            throw new BadRequestException(errorMessage);
        }
        Optional<CustomerDTO> customerDto = this.customerService.getCustomer(account.getCustomerId());
        if (!customerDto.isPresent()) {
            String errorMessage = String.format("Customer with id: %d not found", account.getCustomerId());
            log.severe(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        }
        AccountDTO accountDto = modelMapper.map(account, AccountDTO.class);
        accountDto.setCustomer(customerDto.get());
        return accountDto;
    }
}
