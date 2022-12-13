package com.example.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/payments/")
public class PaymentController {

    @GetMapping(value="paynow/{price}")
    public ResponseEntity<String> paynow(@PathVariable Integer price){
        return ResponseEntity.ok("payment with "+price+"is Successful");
    }
}
