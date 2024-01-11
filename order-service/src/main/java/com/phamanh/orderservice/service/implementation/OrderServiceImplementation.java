package com.phamanh.orderservice.service.implementation;


import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import com.phamanh.orderservice.config.JwtProvider;
import com.phamanh.orderservice.domains.Address;
import com.phamanh.orderservice.domains.Order;
import com.phamanh.orderservice.domains.OrderItem;
import com.phamanh.orderservice.repository.AddressRepository;
import com.phamanh.orderservice.repository.OrderItemRepository;
import com.phamanh.orderservice.repository.OrderRepository;
import com.phamanh.orderservice.request.OrderItemPaymentRequest;
import com.phamanh.orderservice.request.OrderItemRequest;
import com.phamanh.orderservice.request.OrderPaymentRequest;
import com.phamanh.orderservice.request.OrderRequest;
import com.phamanh.orderservice.response.CreateOrderResponse;
import com.phamanh.orderservice.response.OrderResponse;
import com.phamanh.orderservice.request.OrderTopic;
import com.phamanh.orderservice.response.PaymentLinkResponse;
import com.phamanh.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;


    private final OrderItemRepository orderItemRepository;

    private final AddressRepository addressRepository;

    private final RestTemplate restTemplate;

    private final StreamBridge streamBridge;

    private final String CREATE_ORDER_COD_INVENTORY_TOPIC = "createOrderCOD-out-0";

    private final String CREATE_ORDER_TOPIC = "createOrder-out-0";

    private final String CANCEL_ORDER_TOPIC = "cancelOrder-out-0";

    private final String CONFIRM_PAYMENT_ORDER_TOPIC ="confirmPaymentOrder-out-0";
    private final String URl_CHECK_LIST_ORDER_ITEMS = "http://localhost:8080/api/inventory/createOrder/";

    private final String URL_PAYMENT_ORDER = "http://localhost:8090/api/payment/createOrder/";


    @Override
    public CreateOrderResponse createOrder(String jwt, Address shippingAddress, List<OrderItem> orderItems, Order order) {

        OrderResponse orderResponse = createOrderUtil(orderItems, order);

        CreateOrderResponse createOrderResponse = new CreateOrderResponse();

        if (orderResponse.getMessage().equals("Success")) {

            if (order.getPayment().equals(Order.Payment.COD)) {

                Order saveOrder = placeOrder(orderItems, jwt, shippingAddress, order);

                OrderTopic orderTopic = new OrderTopic();
                orderTopic.setOrderId(saveOrder.getId());
                orderTopic.setCode(saveOrder.getCode());

                streamBridge.send(CREATE_ORDER_COD_INVENTORY_TOPIC, orderTopic);

                streamBridge.send(CREATE_ORDER_TOPIC,order);

                createOrderResponse.setMessage("Success");

                return createOrderResponse;
            }

            if (order.getPayment().equals(Order.Payment.CASH)){

                Order saveOrder = placeOrder(orderItems, jwt, shippingAddress, order);

                List<OrderItem> listOrderItems = saveOrder.getOrderItems();

                OrderPaymentRequest orderPaymentRequest = new OrderPaymentRequest();

                List<OrderItemPaymentRequest> orderItemPaymentRequests = new ArrayList<>();

                for (OrderItem orderItem: listOrderItems) {

                    OrderItemPaymentRequest orderItemPaymentRequest = new OrderItemPaymentRequest();

                    orderItemPaymentRequest.setQuantity(orderItem.getQuantity());
                    orderItemPaymentRequest.setTitleProduct(orderItem.getTitleProduct());
                    orderItemPaymentRequest.setSubtotal(orderItem.getSubtotal());

                    orderItemPaymentRequests.add(orderItemPaymentRequest);

                }
                orderPaymentRequest.setId(saveOrder.getId());
                orderPaymentRequest.setCode(saveOrder.getCode());
                orderPaymentRequest.setAccountId(saveOrder.getAccountId());
                orderPaymentRequest.setTotalPay(saveOrder.getTotalPay());
                orderPaymentRequest.setOrderItemPaymentRequests(orderItemPaymentRequests);


                ResponseEntity<PaymentLinkResponse> paymentLinkResponseEntity = restTemplate.postForEntity(URL_PAYMENT_ORDER, orderPaymentRequest, PaymentLinkResponse.class);

                PaymentLinkResponse paymentLinkResponse = paymentLinkResponseEntity.getBody();

                if (paymentLinkResponse.getReturn_code()==1){

                    createOrderResponse.setMessage("Success");
                    createOrderResponse.setPaymentUrl(paymentLinkResponse.getOrder_url());
                }
                if (paymentLinkResponse.getReturn_code()==2){

                    OrderTopic orderTopic = new OrderTopic();
                    orderTopic.setOrderId(saveOrder.getId());
                    orderTopic.setCode(saveOrder.getCode());

                    streamBridge.send(CANCEL_ORDER_TOPIC, orderTopic);

                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            paymentLinkResponse.getReturn_message()+" : "+paymentLinkResponse.getSub_return_message());
                }
                return createOrderResponse ;
            }
        }
        if (orderResponse.getMessage().equals("Fail")){

            createOrderResponse.setMessage("Fail");
            createOrderResponse.setFailDetails(orderResponse.getResults());

            return createOrderResponse;
        }

        return null;

    }

    @Override
    public void confirmPaymentOrder(OrderTopic orderTopic) {

        streamBridge.send(CONFIRM_PAYMENT_ORDER_TOPIC,orderTopic);

        Order orderById = orderRepository.findById(orderTopic.getOrderId()).orElseThrow(()-> new ResourceNotFoundException("Order not valid"));

        orderById.setStatus(Order.Status.PAID);

        Order saveOrder = orderRepository.save(orderById);

        streamBridge.send(CREATE_ORDER_TOPIC,saveOrder);

    }

    @Override
    public void failPaymentOrder(OrderTopic orderTopic) {

        streamBridge.send(CANCEL_ORDER_TOPIC, orderTopic);

        Order order = orderRepository.findById(orderTopic.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order Id Not valid"));
        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);
    }

    protected OrderResponse createOrderUtil(List<OrderItem> orderItems, Order order) {

        String code = UUID.randomUUID().toString();
        order.setCode(code);

        OrderRequest orderRequest = new OrderRequest();

        orderRequest.setOrderCode(code);
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        BeanUtils.copyProperties(orderItems, orderItemRequests);
        orderRequest.setOrderItemsResponse(orderItemRequests);

        ResponseEntity<OrderResponse> orderResponse = restTemplate.postForEntity(URl_CHECK_LIST_ORDER_ITEMS, orderRequest, OrderResponse.class);

        return orderResponse.getBody();

    }

    protected Order placeOrder(List<OrderItem> orderItems,String jwt,Address shippingAddress,Order order){
        Long accountId = JwtProvider.getAccountIdFromToken(jwt);

        Address address = addressRepository.save(shippingAddress);

        double totalPay=0;

        order.setAccountId(accountId);
        order.setOrderItems(orderItems);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());

        for (OrderItem item: orderItems) {
            totalPay+=item.getSubtotal();
            item.setOrder(order);
            item.setStatus(OrderItem.Status.PLACED);
        }

        order.setTotalPay(totalPay);
        order.setStatus(Order.Status.PLACED);

        Order saveOrder = orderRepository.save(order);

        orderItemRepository.saveAll(orderItems);

        return saveOrder;
    }

//    @Override
//    public Order findOrderById(Long orderId) throws OrderException {
//
//        return orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order not exist with id :" + orderId));
//
//    }
//
//    @Override
//    public List<Order> usersOrderHistory(Long userId) {
//
//        return orderRepository.getUsersOrders(userId);
//    }
//
//    @Override
//    public Order placedOrder(Long orderId) throws OrderException {
//
//        Order order = findOrderById(orderId);
//        order.setOrderStatus("PLACED");
//        order.getPaymentDetails().setStatus("COMPLETED");
//
//        return order;
//    }
//
//    @Override
//    public Order confirmedOrder(Long orderId) throws OrderException {
//
//        Order order = findOrderById(orderId);
//        order.setOrderStatus("CONFIRMED");
//
//        return order;
//    }
//
//    @Override
//    public Order shippedOrder(Long orderId) throws OrderException {
//
//        Order order = findOrderById(orderId);
//        order.setOrderStatus("SHIPPED");
//
//        return orderRepository.save(order);
//    }
//
//    @Override
//    public Order deliveredOrder(Long orderId) throws OrderException {
//
//        Order order = findOrderById(orderId);
//        order.setOrderStatus("DELIVERED");
//
//        return orderRepository.save(order);
//    }
//
//    @Override
//    public Order canceledOrder(Long orderId) throws OrderException {
//
//        Order order = findOrderById(orderId);
//        order.setOrderStatus("CANCELLED");
//
//        return orderRepository.save(order);
//    }
//
//    @Override
//    public List<Order> getAllOrders() {
//
//        return orderRepository.findAll();
//    }
//
//    @Override
//    public void deleteOrder(Long orderId) throws OrderException {
//        Order order = findOrderById(orderId);
//        orderRepository.delete(order);
//    }

}
