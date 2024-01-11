package com.phamanh.accountservice.domains;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "confirmation_code")
@Entity
public class ConfirmationCode {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "code",columnDefinition = "text",nullable = false)
    private String code;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;
    public enum Status {
        EXPIRED,
        USED
    }
}
