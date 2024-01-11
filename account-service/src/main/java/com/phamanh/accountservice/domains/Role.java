package com.phamanh.accountservice.domains;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "role")
@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name",nullable = false)
    private Name name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<Account> accounts;

    public enum Name {
        ADMIN,
        CUSTOMER,
        SALE
    }

}

