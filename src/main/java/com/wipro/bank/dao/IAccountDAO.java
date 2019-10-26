package com.wipro.bank.dao;

import com.wipro.bank.bean.AccountDTO;

import java.util.List;
import java.util.Optional;

public interface IAccountDAO {
    AccountDTO saveAccount(AccountDTO accountDTO);
    Optional<AccountDTO> findById(Integer id);
    List<AccountDTO> findAll();
    long count();
    void delete(AccountDTO accountDTO);
    boolean existsById(Integer id);
}
