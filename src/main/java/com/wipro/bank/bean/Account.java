package com.wipro.bank.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int accountId;
    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
    private Double balance;
}
