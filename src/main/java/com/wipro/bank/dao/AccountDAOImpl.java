package com.wipro.bank.dao;

import com.wipro.bank.bean.AccountDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("JpaQlInspection")
@Repository
public class AccountDAOImpl implements IAccountDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AccountDTO saveAccount(AccountDTO account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public Optional<AccountDTO> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(AccountDTO.class, id));
    }

    @Override
    public List<AccountDTO> findAll() {
        return entityManager.createQuery("select a from AccountDTO a", AccountDTO.class)
                .getResultList();
    }

    @Override
    public List<AccountDTO> findByCustomerId(Integer customerId) {
        return entityManager.createQuery("select a from AccountDTO a where a.id =: id", AccountDTO.class)
                .setParameter("id", customerId)
                .getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(a) from AccountDTO a", Long.class)
                .getSingleResult();
    }

    @Override
    public void delete(AccountDTO account) {
        entityManager.remove(account);
    }
}
