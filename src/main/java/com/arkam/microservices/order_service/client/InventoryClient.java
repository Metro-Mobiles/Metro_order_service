package com.arkam.microservices.order_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "inventory", url = "${inventory.url}")
public interface InventoryClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/inventory/checkStock")
    boolean isInStock(@RequestParam String skuCode , @RequestParam Integer quantity);

    @RequestMapping(method = RequestMethod.PUT, value = "/api/inventory/reduceQuantity")
    String deleteByOrder(@RequestParam String skuCode, @RequestParam Integer quantity);
}
