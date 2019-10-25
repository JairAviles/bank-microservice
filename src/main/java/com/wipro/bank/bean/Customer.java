package com.wipro.bank.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customer_profile")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int customerId;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "customer")
    private Set<Account> accounts;
}
