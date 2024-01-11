package com.phamanh.paymentservice.controller;

import com.phamanh.paymentservice.request.OrderPaymentRequest;
import com.phamanh.paymentservice.response.PaymentLinkResponse;
import com.phamanh.paymentservice.service.PaymentService;
import com.phamanh.paymentservice.util.PaymentConstant;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/createOrder")
    public ResponseEntity<PaymentLinkResponse> getPaymentLinkResponse(@RequestBody OrderPaymentRequest orderPaymentRequest) {

        return new ResponseEntity<>(paymentService.createPayment(orderPaymentRequest), HttpStatus.OK);
    }

    @PostMapping("/callback")
    public String callback(@RequestBody String jsonStr) {
        return paymentService.callBack(jsonStr);
    }
}


