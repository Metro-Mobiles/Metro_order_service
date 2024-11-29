package com.arkam.microservices.order_service.controller;


//import groovy.util.logging.Slf4j;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.retry.annotation.Retry;
//import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import com.arkam.microservices.order_service.dto.OrderRequest;
import com.arkam.microservices.order_service.model.Order;
import com.arkam.microservices.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@Valid @RequestBody Order orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

    @GetMapping("/getOrderByUser")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> retrieveOrderByUser(@RequestParam Long userId){
        return ResponseEntity.ok().body(orderService.retrieveOrder(userId));
    }

    @GetMapping("/getOrders")
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> retrieveOrders(){
        return ResponseEntity.ok().body(orderService.findAllOrder());
    }


    @GetMapping("/getOrderBySkuCode")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> retrieveOrderBySkuCode(@RequestParam String skuCode){
        return ResponseEntity.ok().body(orderService.retrieveOrderBySkuCode(skuCode));
    }

    @DeleteMapping("/deleteOrder")
    public String deleteOrder(@RequestParam Long id){
        orderService.deleteOrder(id);
        return "Delete Order Successfully!";
    }

    @PutMapping("/updateOrder")
    public String updateOrder(@RequestParam Long id, @RequestBody Order order){
        orderService.updateOrder(id,order);
        return "Order Updated successfully";
    }

//    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
//        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
//    }
}
