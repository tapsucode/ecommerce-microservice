package com.phamanh.paymentservice.service.implementation;

import com.phamanh.paymentservice.domains.PaymentDetails;
import com.phamanh.paymentservice.repository.PaymentDetailsRepository;
import com.phamanh.paymentservice.request.OrderTopic;
import com.phamanh.paymentservice.response.PaymentLinkResponse;
import com.phamanh.paymentservice.request.OrderPaymentRequest;
import com.phamanh.paymentservice.service.PaymentService;
import com.phamanh.paymentservice.util.HMACUtil;
import com.phamanh.paymentservice.util.PaymentConstant;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements PaymentService {

    private final PaymentDetailsRepository paymentDetailsRepository;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Mac HmacSHA256;

    private final StreamBridge streamBridge;

    private final String CONFIRM_PAYMENT_ORDER_TOPIC = "confirmPaymentOrder-out-0";


    public PaymentServiceImplementation(PaymentDetailsRepository paymentDetailsRepository,StreamBridge streamBridge) throws Exception {
        this.paymentDetailsRepository = paymentDetailsRepository;
         this.streamBridge = streamBridge;
        HmacSHA256 = Mac.getInstance("HmacSHA256");
        HmacSHA256.init(new SecretKeySpec(PaymentConstant.key_2.getBytes(), "HmacSHA256"));
    }

    @Override
    public PaymentLinkResponse createPayment(OrderPaymentRequest orderPaymentRequest) {

        Map<String, Object> order = createOrder(orderPaymentRequest);

        PaymentLinkResponse paymentLinkResponse = createPayment(order);

        if (paymentLinkResponse.getReturn_code()==1){

            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setApp_trans_id(order.get("app_trans_id").toString());
            paymentDetails.setOrderId(orderPaymentRequest.getId());
            paymentDetails.setAccountId(orderPaymentRequest.getAccountId());
            paymentDetails.setPaymentUlr(paymentLinkResponse.getOrder_url());
            paymentDetails.setOrderCode(orderPaymentRequest.getCode());
            paymentDetails.setOrder_token(paymentLinkResponse.getOrder_token());
            paymentDetails.setStatus(PaymentDetails.Status.PROCESSING);
            String createAt = getCurrentTimeString("yyyy-MM-dd HH:mm:ss");
            paymentDetails.setCrateAt(createAt);
            paymentDetailsRepository.save(paymentDetails);
        }


        return paymentLinkResponse;

    }

    protected Map<String, Object>  createOrder(OrderPaymentRequest orderPaymentRequest){

        Random rand = new Random();
        int random_id = rand.nextInt(1000000);

        Map<String, Object> embed_data = new HashMap<>();
        Map<String, Object> order = new HashMap<String, Object>() {};

        order.put("app_id", PaymentConstant.app_id);
        order.put("app_trans_id", getCurrentTimeString("yyMMdd") + "_" + random_id); // translation missing: vi.docs.shared.sample_code.comments.app_trans_id
        order.put("app_time", System.currentTimeMillis()); // milliseconds
        order.put("app_user", PaymentConstant.app_user);
        order.put("amount", orderPaymentRequest.getTotalPay());
        order.put("description",PaymentConstant.description  + random_id);
        order.put("bank_code", "zalopayapp");
        order.put("callback_url",PaymentConstant.callback_url);
        order.put("item", new JSONObject(orderPaymentRequest.getOrderItemPaymentRequests()).toString());
        embed_data.put("redirecturl",PaymentConstant.redirecturl);
        order.put("embed_data", new JSONObject(embed_data).toString());

        // app_id +”|”+ app_trans_id +”|”+ appuser +”|”+ amount +"|" + app_time +”|”+ embed_data +"|" +item
        String data = order.get("app_id") + "|" + order.get("app_trans_id") + "|" + order.get("app_user") + "|" + order.get("amount")
                + "|" + order.get("app_time") + "|" + order.get("embed_data") + "|" + order.get("item");
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, PaymentConstant.key_1, data));

        return order;
    }

    protected PaymentLinkResponse createPayment(Map<String, Object> order){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> mapOrder= new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> e : order.entrySet()) {
            mapOrder.add(e.getKey(), e.getValue().toString());
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mapOrder, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(PaymentConstant.endpoint, request, String.class);

        String resultJsonStr = response.getBody();
        JSONObject result = new JSONObject(resultJsonStr);

        int return_code = (int) result.get("return_code");
        String return_message = (String) result.get("return_message");
        String order_url = (String) result.get("order_url");
        int sub_return_code = (int) result.get("sub_return_code");
        String sub_return_message = (String) result.get("sub_return_message");
        String zp_trans_token = (String) result.get("zp_trans_token");
        String order_token = (String) result.get("order_token");
        String qr_code = (String) result.get("qr_code");

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        paymentLinkResponse.setReturn_code(return_code);
        paymentLinkResponse.setReturn_message(return_message);
        paymentLinkResponse.setSub_return_code(sub_return_code);
        paymentLinkResponse.setSub_return_message(sub_return_message);
        paymentLinkResponse.setOrder_url(order_url);
        paymentLinkResponse.setZp_trans_token(zp_trans_token);
        paymentLinkResponse.setOrder_token(order_token);
        paymentLinkResponse.setQr_code(qr_code);
        return paymentLinkResponse;
    }

    @Override
    public String callBack(String jsonStr) {

        JSONObject result = new JSONObject();

        try {
            JSONObject order = new JSONObject(jsonStr);
            String dataOrder = order.getString("data");
            String reqMacOrder = order.getString("mac");

            byte[] hashBytes = HmacSHA256.doFinal(dataOrder.getBytes());
            String mac = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();

            // kiểm tra callback hợp lệ (đến từ ZaloPay server)
            if (!reqMacOrder.equals(mac)) {
                // callback không hợp lệ
                result.put("return_code", -1);
                result.put("return_message", "mac not equal");
            } else {
                // thanh toán thành công
                // merchant cập nhật trạng thái cho đơn hàng
                JSONObject data = new JSONObject(dataOrder);
                String app_trans_id = data.getString("app_trans_id");
                logger.info("update order's status = success where app_trans_id = " + app_trans_id);

                PaymentDetails paymentDetails = paymentDetailsRepository.findByApp_Trans_Id(app_trans_id);

                paymentDetails.setStatus(PaymentDetails.Status.SUCCESS);
                paymentDetailsRepository.save(paymentDetails);

                OrderTopic orderTopic = new OrderTopic();
                orderTopic.setCode(paymentDetails.getOrderCode());
                orderTopic.setOrderId(paymentDetails.getOrderId());

                streamBridge.send(CONFIRM_PAYMENT_ORDER_TOPIC,orderTopic);

                result.put("return_code", 1);
                result.put("return_message", "success");
            }
        } catch (Exception ex) {
            result.put("return_code", 0); // ZaloPay server sẽ callback lại (tối đa 3 lần)
            result.put("return_message", ex.getMessage());
        }

        // thông báo kết quả cho ZaloPay server
        return result.toString();
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(new Date());
    }
}
