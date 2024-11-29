package com.arkam.microservices.order_service.service;

import com.arkam.microservices.order_service.client.InventoryClient;
//import com.arkam.microservices.order_service.client.ProductClient;
import com.arkam.microservices.order_service.client.UserClient;
import com.arkam.microservices.order_service.dto.OrderRequest;
import com.arkam.microservices.order_service.model.Order;
import com.arkam.microservices.order_service.model.UserRequest;
import com.arkam.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
   private final InventoryClient inventoryClient;
   private final UserClient userClient;
//   private final ProductClient productClient;
    //private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(Order orderRequest) {
        try {
//            UserRequest userRequest = new UserRequest();
//            userRequest.setUserID(orderRequest.getUserId());

            var isUserAvailable = userClient.checkUserId(orderRequest.getUserId());
            if(isUserAvailable) {
                var isProductInStock = inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity());
                if (isProductInStock) {
//            Order order = new Order();
//            order.setOrderNumber(UUID.randomUUID().toString());
//            order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
//            order.setSkuCode(orderRequest.skuCode());
//            order.setQuantity(orderRequest.quantity());
//            order.setUserId(orderRequest.userId());

                    orderRepository.save(orderRequest);
                    inventoryClient.deleteByOrder(orderRequest.getSkuCode(), orderRequest.getQuantity());
                    // Send the message to Kafka Topic
//            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
//            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
//            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
//            orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
//            orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
//            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
//            kafkaTemplate.send("order-placed", orderPlacedEvent);
//            log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
                } else {
                    throw new RuntimeException("Product with SkuCode " + orderRequest.getSkuCode() + " is not in stock");
                }
//            return ResponseEntity.ok().body("Success..");
            } else {
                throw new RuntimeException("User not exist with "+orderRequest.getUserId());
            }

        } catch (Exception e) {
            throw new RuntimeException("System error:" + e.getMessage(), e);
        }
    }

    public ResponseEntity<?> findAllOrder(){
        try {
            List<Order> order = orderRepository.findAll();
            return ResponseEntity.ok().body(order);
        } catch (Exception e){
            return ResponseEntity.ok().body("No any request there");
        }
    }

    public void deleteOrder(Long id){
        try {
            orderRepository.findById(id);
        } catch (Exception e){
            throw new RuntimeException("Missing order:" + e.getMessage(),e);
        }
    }

    public ResponseEntity<?> retrieveOrder(Long userid){
        try {
            Optional<Order> order = orderRepository.findByUserId(userid);
            return ResponseEntity.ok().body(order);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Order not found:"+userid+"Error:"+e.getMessage());
        }
    }

    public ResponseEntity<?> retrieveOrderBySkuCode(String skuCode){
        try {
            Optional<Order> order = orderRepository.findBySkuCode(skuCode);
            return ResponseEntity.ok().body(order);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Order not found:"+skuCode+"Error:"+e.getMessage());
        }
    }

    public void updateOrder(Long id, Order order){
        try {

//            UserRequest userRequest = new UserRequest();
//            userRequest.setUserID(order.getUserId());

            var isUserPreset = userClient.checkUserId(order.getUserId());
            if(isUserPreset) {
                Optional<Order> userOrder = orderRepository.findById(id);
                if (userOrder.isPresent()) {
                    Order exitUserOrder = userOrder.get();
                    exitUserOrder.setPrice(order.getPrice());
                    exitUserOrder.setQuantity(order.getQuantity());

                    orderRepository.save(exitUserOrder);
                } else {
                    throw new RuntimeException("Order isn't present:" + id);
                }
            } else {
                throw new RuntimeException("User not present");
            }
        } catch (Exception e){
            throw new RuntimeException("Order not found:"+id+e.getMessage(),e);
        }
    }
}