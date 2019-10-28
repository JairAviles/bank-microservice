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

    /**
     * Persist an CustomerDTO as parameter
     * @param customer
     * @return customer with generated Id
     */
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        entityManager.merge(customer);
        return customer;
    }

    /**
     * Find CustomerDTO by Id
     * @param id
     * @return Optional<CustomerDTO>
     */
    @Override
    public Optional<CustomerDTO> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(CustomerDTO.class, id));
    }

    /**
     * Find All existing CustomerDTOs
     * @return List<CustomerDTO>
     */
    @Override
    public List<CustomerDTO> findAll() {
        return entityManager.createQuery("select c from CustomerDTO c", CustomerDTO.class)
                .getResultList();
    }

    /**
     * Count total of CustomerDTO in DB
     * @return long total
     */
    @Override
    public long count() {
        return entityManager.createQuery("select count(c) from CustomerDTO c", Long.class)
                    .getSingleResult();
    }

    /**
     * Delete an CustomerDTO as parameter
     * @param customer
     */
    @Override
    public void delete(CustomerDTO customer) {
        entityManager.remove(customer);
    }

    /**
     * Return boolean if id customer exists in DB
     * @param id
     * @return boolean
     */
    @Override
    public boolean existsById(Integer id) {
        Long count = entityManager.createQuery(
                "select count(c.id) FROM CustomerDTO c where c.id =: id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
