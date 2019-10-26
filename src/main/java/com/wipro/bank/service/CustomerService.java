package com.wipro.bank.service;

import com.wipro.bank.bean.CustomerDTO;
import com.wipro.bank.dao.ICustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    @Autowired
    @Qualifier("customerDAOImpl")
    private ICustomerDAO dao;

    /**
     * Method that calls the save method from persistence layer
     * to create a new customer
     * @param customer
     * @return CustomerDTO
     */
    @Transactional
    public CustomerDTO addCustomer(CustomerDTO customer) {
        return this.dao.saveCustomer(customer);
    }

    /**
     * Method that calls the delete method from persistence layer
     * to remove an existing customer
     * @param customer
     */
    @Transactional
    public void removeCustomer(CustomerDTO customer) {
        this.dao.delete(customer);
    }

    /**
     * Method that calls the find id from persistence layer
     * @param id
     * @return Optional<CustomerDTO>
     */
    public Optional<CustomerDTO> getCustomer(Integer id) {
        return this.dao.findById(id);
    }

    /**
     * Gets all existing CustomerDTOs from DB
     * @return List<CustomerDTO>
     */
    public List<CustomerDTO> getAllCustomers() {
        return this.dao.findAll();
    }

    /**
     * Method that validates if customer id
     * exists in DB
     * @param id
     * @return boolean
     */
    public boolean exists(Integer id) {
        return this.dao.existsById(id);
    }
}
