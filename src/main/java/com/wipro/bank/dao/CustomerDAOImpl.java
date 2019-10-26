package com.wipro.bank.dao;

import com.wipro.bank.bean.CustomerDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("JpaQlInspection")
@Repository
public class CustomerDAOImpl implements ICustomerDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        entityManager.persist(customer);
        return customer;
    }

    @Override
    public Optional<CustomerDTO> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(CustomerDTO.class, id));
    }

    @Override
    public List<CustomerDTO> findAll() {
        return entityManager.createQuery("select c from CustomerDTO c", CustomerDTO.class)
                .getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("select count(c) from CustomerDTO c", Long.class)
                    .getSingleResult();
    }

    @Override
    public void delete(CustomerDTO customer) {
        entityManager.remove(customer);
    }

    @Override
    public boolean existsById(Integer id) {
        Long count = entityManager.createQuery(
                "select count(c.id) FROM CustomerDTO c where c.id =: id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
