package com.wipro.bank.bean;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "account")
public class AccountDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int accountId;
    @ManyToOne
    @JoinColumn(name = "customerId")
    @NonNull
    private CustomerDTO customer;
    @NonNull
    private Double balance;
}
