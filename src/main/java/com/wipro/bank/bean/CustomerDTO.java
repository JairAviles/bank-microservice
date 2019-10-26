package com.wipro.bank.bean;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "customer_profile")
public class CustomerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int customerId;
    @Column(name = "name")
    @NonNull
    private String name;

    @OneToMany(mappedBy = "customer")
    private Set<AccountDTO> accounts;

    public CustomerDTO(@NonNull String name) {
        this.name = name;
    }

}
