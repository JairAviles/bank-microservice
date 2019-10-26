package com.wipro.bank.dao;

import com.wipro.bank.bean.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface ICustomerDAO {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    Optional<CustomerDTO> findById(Integer id);
    List<CustomerDTO> findAll();
    long count();
    void delete(CustomerDTO customerDTO);
    boolean existsById(Integer id);
}
