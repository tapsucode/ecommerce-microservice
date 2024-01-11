package com.phamanh.notificationservice.service.impl;

import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import com.phamanh.notificationservice.data.MailInfo;
import com.phamanh.notificationservice.data.ProfileOrderItem;
import com.phamanh.notificationservice.request.Order;
import com.phamanh.notificationservice.request.OrderItem;
import com.phamanh.notificationservice.response.OrderItemResponse;
import com.phamanh.notificationservice.response.OrderResponse;
import com.phamanh.notificationservice.response.ProductResponse;
import com.phamanh.notificationservice.response.ProfileAccountResponse;
import com.phamanh.notificationservice.service.EmailService;
import com.phamanh.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The notificationOrder method performs the function of sending order information to the corresponding stores when there is an order, then sends each store's order information via email.
 * The parameter passed in is an Order with a void return value .
 * The convertOrderItems method takes an order, filters it and converts List<OrderItem> to Map<Long, ProfileOrderItem> with key being the shop id and value being ProfileOrderItem.
 * The notificationOrder method receives an order, retrieves the order's userName and email information .
 * Calls the convertOrderItems function to filter items in the same shop and retrieves information for each product .
 * Then calls the sendEmail method with the information. Order information and shop email.
 **/
@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private static final String NOTIFICATION_SALE_INFORMATION_ORDER_CONTENT = "email.notification.information.order.content";
    private static final String NOTIFICATION_SALE_INFORMATION_ORDER_SUBJECT = "email.notification.information.order.subject";

    private final String URL_GET_PROFILE_BY_ID = "/api/v1/admin/account//profile/{accountId}";

    private final String URL_GET_PRODUCT_BY_ID = "http://localhost:8020/api/products/check/{productId}";

    private final EmailService emailService;

    private final MessageSource messageSource;

    private final RestTemplate restTemplate;

    @Override
    public void notificationOrder(Order order) {

        OrderResponse orderResponse = new OrderResponse();

        BeanUtils.copyProperties(order, orderResponse);

        ResponseEntity<ProfileAccountResponse> response = restTemplate.getForEntity(URL_GET_PROFILE_BY_ID, ProfileAccountResponse.class,order.getId());
        ProfileAccountResponse profileAccountResponse = response.getBody();

        orderResponse.setEmail(profileAccountResponse.getEmail());
        orderResponse.setUserName(profileAccountResponse.getEmail());

        Map<Long, ProfileOrderItem> mapItems = convertOrderItems(order);

        mapItems.forEach((key,value)->{

            ResponseEntity<ProfileAccountResponse> response1 = restTemplate.getForEntity(URL_GET_PROFILE_BY_ID, ProfileAccountResponse.class,key);
            ProfileAccountResponse profileAccountResponse1 = response1.getBody();

            List<OrderItemResponse> orderItemResponses = value.getOrderItemResponse();

            orderResponse.setTotalPay(value.getTotalPay());
            orderResponse.setOrderItemResponses(orderItemResponses);

            sendEmail(orderResponse,profileAccountResponse1.getEmail());

        });

    }

    protected Map<Long, ProfileOrderItem> convertOrderItems(Order order) {

        List<OrderItem> orderItems = order.getOrderItems();
        Map<Long, ProfileOrderItem> mapItems = new HashMap<>();

        orderItems.forEach(item -> {

            Long accountProductId = item.getAccountProduct();

            ProductResponse productResponse = checkProductExist(item.getProductId());

            OrderItemResponse orderItemResponse = new OrderItemResponse();

            BeanUtils.copyProperties(item, orderItemResponse);
            orderItemResponse.setProductResponse(productResponse);

            ProfileOrderItem profileOrderItem;

            if (mapItems.containsKey(accountProductId)) {

                profileOrderItem = mapItems.get(accountProductId);
                profileOrderItem.setTotalPay(profileOrderItem.getTotalPay() + item.getSubtotal());
                List<OrderItemResponse> orderItems1 = profileOrderItem.getOrderItemResponse();
                orderItems1.add(orderItemResponse);
                profileOrderItem.setOrderItemResponse((orderItems1));

            } else {

                profileOrderItem = new ProfileOrderItem();
                profileOrderItem.setTotalPay(item.getSubtotal());
                List<OrderItemResponse> orderItems1 = profileOrderItem.getOrderItemResponse();
                orderItems1.add(orderItemResponse);
                profileOrderItem.setOrderItemResponse((orderItems1));

            }

            mapItems.put(accountProductId, profileOrderItem);
        });

        return mapItems;
    }

    protected ProductResponse checkProductExist(Long productId) {

        ResponseEntity<ProductResponse> productResponseEntity = restTemplate.getForEntity(URL_GET_PRODUCT_BY_ID, ProductResponse.class, productId);
        ProductResponse product = productResponseEntity.getBody();

        if (product == null) {
            throw new ResourceNotFoundException("Product not exists");
        }
        return product;
    }

    protected void sendEmail(OrderResponse orderResponse,String email){

        Locale locale = LocaleContextHolder.getLocale();
        // Set values in the HTML form 'form-send-email-register-user'
        Map<String, Object> data = new HashMap<>();

        data.put("content", messageSource.getMessage(NOTIFICATION_SALE_INFORMATION_ORDER_CONTENT, null, locale));

        data.put("order",orderResponse);

        // Send email using the template form

        MailInfo mailInfo = new MailInfo();
        mailInfo.addTo(email);
        mailInfo.setSubject(messageSource.getMessage(NOTIFICATION_SALE_INFORMATION_ORDER_SUBJECT, null, locale));
        mailInfo.setHtml(true);
        emailService.sendHtmlMail(mailInfo, "form-notification-order", data);
    }
}
