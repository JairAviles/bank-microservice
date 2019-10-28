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

    /**
     * Persist an AccountDTO as parameter
     * @param account
     * @return account with generated Id
     */
    @Override
    public AccountDTO saveAccount(AccountDTO account) {
        entityManager.merge(account);
        return account;
    }

    /**
     * Find AccountDTO by Id
     * @param id
     * @return Optional<AccountDTO>
     */
    @Override
    public Optional<AccountDTO> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(AccountDTO.class, id));
    }

    /**
     * Find All existing AccountDTOs
     * @return List<AccountDTO>
     */
    @Override
    public List<AccountDTO> findAll() {
        return entityManager.createQuery("select a from AccountDTO a", AccountDTO.class)
                .getResultList();
    }

    /**
     * Count total of AccountDTOs in DB
     * @return long total
     */
    @Override
    public long count() {
        return entityManager.createQuery("select count(a) from AccountDTO a", Long.class)
                .getSingleResult();
    }

    /**
     * Delete an AccountDTO as parameter
     * @param account
     */
    @Override
    public void delete(AccountDTO account) {
        entityManager.remove(account);
    }

    /**
     * Return boolean if id customer exists in DB
     * @param id
     * @return boolean
     */
    @Override
    public boolean existsById(Integer id) {
        Long count = entityManager.createQuery(
                "select count(a.id) FROM AccountDTO a where a.id =: id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
