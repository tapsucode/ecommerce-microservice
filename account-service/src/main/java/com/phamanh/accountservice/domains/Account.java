package com.phamanh.accountservice.domains;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "account")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false,columnDefinition = "text",unique = true)
    private String username;

    @Column(name = "password",nullable = false,columnDefinition = "text")
    private String password;

    @Column(name = "email",nullable = false,columnDefinition = "text",unique = true)
    private String email;

    @Column(name = "full_name",nullable = false,columnDefinition = "text")
    private String fullName;

    @Column(name = "address",nullable = true,columnDefinition = "text")
    private String address;

    @Column(name = "phone_number",nullable = true,columnDefinition = "text")
    private String phoneNumber;

    private LocalDateTime createAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany()
    @JoinTable(name = "account_role",joinColumns = @JoinColumn(name = "account_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "account")
    private List<ConfirmationCode> confirmationCodes;

    public enum Status {
        PENDING,
        ACTIVATED,
        DEACTIVATED
    }
}
