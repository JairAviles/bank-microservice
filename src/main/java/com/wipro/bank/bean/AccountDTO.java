package com.wipro.bank.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(value = { "customer" })
@EqualsAndHashCode(exclude="customer")
@Table(name = "account")
public class AccountDTO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int accountId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    @NonNull
    @JsonBackReference
    private CustomerDTO customer;
    @NonNull
    private Double balance;
}
