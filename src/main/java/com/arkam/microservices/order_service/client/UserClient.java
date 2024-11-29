package com.arkam.microservices.order_service.client;


//import com.arkam.microservices.user_service.DTO.Response;
import com.arkam.microservices.order_service.model.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user",url = "${user.url}")
public interface UserClient {


    @RequestMapping(method = RequestMethod.GET,value = "/auth/retriveByUserId")
    Boolean checkUserId(@RequestParam("userID") Long userId);
}
