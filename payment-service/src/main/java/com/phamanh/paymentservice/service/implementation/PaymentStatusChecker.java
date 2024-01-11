package com.phamanh.paymentservice.service.implementation;

import com.phamanh.paymentservice.domains.PaymentDetails;
import com.phamanh.paymentservice.repository.PaymentDetailsRepository;
import com.phamanh.paymentservice.request.OrderTopic;
import com.phamanh.paymentservice.util.HMACUtil;
import com.phamanh.paymentservice.util.PaymentConstant;
import lombok.RequiredArgsConstructor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class PaymentStatusChecker {

    private static final long CHECK_INTERVAL_MS = 3 * 60 * 1000;

    private final RestTemplate restTemplate;

    private final PaymentDetailsRepository paymentDetailsRepository;

    private final StreamBridge streamBridge;

    private final String CONFIRM_PAYMENT_ORDER_TOPIC = "confirmPaymentOrder-out-0";

    private final String FAILED_PAYMENT_ORDER_TOPIC ="failPaymentOrder-out-0";

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Scheduled(fixedDelay = CHECK_INTERVAL_MS)
    public CompletableFuture<Void> checkPaymentStatus(){

        List<PaymentDetails> paymentDetails = paymentDetailsRepository.findByStatus(PaymentDetails.Status.PROCESSING);

        for (PaymentDetails paymentDetail: paymentDetails) {

            String crateAt = paymentDetail.getCrateAt();

            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date parsedDate = newFormat.parse(crateAt);
                Calendar cal = Calendar.getInstance();
                cal.setTime(parsedDate);

                cal.add(Calendar.MINUTE, 15);
                Date newTime = cal.getTime();

                // Kiểm tra xem đã qua 15 phút hay chưa
                if (!newTime.before(new Date())) {

                    JSONObject result = checkPaymentOrder(paymentDetail);

                    int returnCode = result.getInt("return_code");
                    String returnMessage = result.getString("return_message");

                    String subReturnMessage = result.getString("sub_return_message");

                    switch (returnCode){

                        case 1:{

                            paymentDetail.setStatus(PaymentDetails.Status.SUCCESS);
                            paymentDetailsRepository.save(paymentDetail);

                            OrderTopic orderTopic = createOrderTopic(paymentDetail);

                            streamBridge.send(CONFIRM_PAYMENT_ORDER_TOPIC,orderTopic);
                            break;
                        }
                        case 2:{
                            paymentDetail.setStatus(PaymentDetails.Status.CANCELLED);
                            paymentDetailsRepository.save(paymentDetail);

                            logger.info( paymentDetail.getApp_trans_id()+returnMessage+":"+subReturnMessage);

                            OrderTopic orderTopic = createOrderTopic(paymentDetail);

                            streamBridge.send(FAILED_PAYMENT_ORDER_TOPIC,orderTopic);

                            break;
                        }
                        case 3:break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + returnCode);
                    }

                } else {
                    paymentDetail.setStatus(PaymentDetails.Status.CANCELLED);
                    paymentDetailsRepository.save(paymentDetail);

                    OrderTopic orderTopic = createOrderTopic(paymentDetail);


                    streamBridge.send(FAILED_PAYMENT_ORDER_TOPIC,orderTopic);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return CompletableFuture.completedFuture(null);
    }

    protected JSONObject checkPaymentOrder(PaymentDetails paymentDetail){

        String app_trans_id = paymentDetail.getApp_trans_id();
        String data = String.format("%s|%s|%s", PaymentConstant.app_id, app_trans_id, PaymentConstant.key_1);
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, PaymentConstant.key_1, data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        List<NameValuePair> params = List.of(
                new BasicNameValuePair("app_id", PaymentConstant.app_id),
                new BasicNameValuePair("app_trans_id", app_trans_id),
                new BasicNameValuePair("mac", mac)
        );

        HttpEntity<List<NameValuePair>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(PaymentConstant.CHECK_PAYMENT_API,String.class,requestEntity );

        // Parse JSON response
        JSONObject result = new JSONObject(responseEntity.getBody());

        return result;
    }

    protected OrderTopic createOrderTopic(PaymentDetails paymentDetail){

        OrderTopic orderTopic = new OrderTopic();
        orderTopic.setCode(paymentDetail.getOrderCode());
        orderTopic.setOrderId(paymentDetail.getOrderId());

        return orderTopic;
    }
}
