package com.phamanh.orderservice.controller;

import com.phamanh.accountservice.demo.exception.OrderException;
import com.phamanh.accountservice.demo.exception.UserException;
import com.phamanh.accountservice.demo.response.PaymentLinkResponse;
import com.phamanh.accountservice.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPayment(@PathVariable()Long orderId, @RequestHeader("Authorization")String jwt) throws UserException, OrderException {

        PaymentLinkResponse paymentLinkResponse = paymentService.createPayment(orderId,jwt);


        if (paymentLinkResponse.getOrderUrl() == null){
            return new ResponseEntity<>(paymentLinkResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(paymentLinkResponse, HttpStatus.CREATED);
    }
}
