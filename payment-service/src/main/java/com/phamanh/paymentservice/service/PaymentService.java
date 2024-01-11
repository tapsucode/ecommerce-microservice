package com.phamanh.paymentservice.service;

import com.phamanh.paymentservice.response.PaymentLinkResponse;
import com.phamanh.paymentservice.request.OrderPaymentRequest;

public interface PaymentService {

    public PaymentLinkResponse createPayment(OrderPaymentRequest orderPaymentRequest);

    public String callBack(String jsonStr);
}
