package com.wipro.bank.bean;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class AccountDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int accountId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId")
    @NonNull
    private CustomerDTO customer;
    @NonNull
    private Double balance;

    public AccountDTO(@NonNull CustomerDTO customer, @NonNull Double balance) {
        this.customer = customer;
        this.balance = balance;
    }
}
