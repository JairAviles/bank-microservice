package com.wipro.bank.service;

import com.wipro.bank.bean.AccountDTO;
import com.wipro.bank.dao.IAccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountService {
    @Autowired
    @Qualifier("accountDAOImpl")
    private IAccountDAO dao;

    /**
     * Method that calls the save method from persistence layer
     * to create a new account
     * @param account
     * @return AccountDTO
     */
    @Transactional
    public AccountDTO addAccount(AccountDTO account) {
        return this.dao.saveAccount(account);
    }

    /**
     * Method that calls the delete method from persistence layer
     * to remove an existing account
     * @param account
     */
    @Transactional
    public void removeAccount(AccountDTO account) {
        this.dao.delete(account);
    }

    /**
     * Method that calls the find id from persistence layer
     * @param id
     * @return Optional<AccountDTO>
     */
    public Optional<AccountDTO> getBalanceOf(Integer id) {
        return this.dao.findById(id);
    }

    /**
     * Gets all existing AccountDTOs from DB
     * @return List<AccountDTO>
     */
    public List<AccountDTO> getAllAccounts() {
        return this.dao.findAll();
    }

    public String transferFunds(int from, int to, double amount) {

        return "";
    }
}
