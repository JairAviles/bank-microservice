package com.wipro.bank.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "account")
@JsonIgnoreProperties(value = { "customer" })
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
