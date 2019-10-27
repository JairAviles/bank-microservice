package com.wipro.bank.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "customer_profile")
public class CustomerDTO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NonNull
    private int customerId;
    @NonNull
    @Column(name = "name")
    private String name;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "customer", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<AccountDTO> accounts;

}
