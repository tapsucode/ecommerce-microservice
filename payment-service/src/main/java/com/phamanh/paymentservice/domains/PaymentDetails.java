package com.phamanh.paymentservice.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app_trans_id;

    private String PaymentUlr;

    private Long orderId;

    private String orderCode;

    private Long accountId;

    private String order_token;

    private Long user_fee_amount;

    private Long discount_amount;

    private String crateAt;

    private String message;

    private Status status;

    public enum Status {
        PROCESSING,
        CANCELLED,
        SUCCESS

    }

}
