package com.wipro.bank.configuration;

import com.wipro.bank.dao.AccountDAOImpl;
import com.wipro.bank.dao.CustomerDAOImpl;
import com.wipro.bank.dao.IAccountDAO;
import com.wipro.bank.dao.ICustomerDAO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration {

    @Bean
    public ICustomerDAO customerDAOImpl() {
        return new CustomerDAOImpl();
    }

    @Bean
    public IAccountDAO accountDAOImpl() {
        return new AccountDAOImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
