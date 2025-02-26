package com.miracle.keoffor.controller;

import com.miracle.keoffor.model.dtos.PaymentResponseDto;
import com.miracle.keoffor.service.custom.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/customer-bookings")
    public ResponseEntity<List<PaymentResponseDto>> getCustomerBookings(
            @RequestParam String email,
            @RequestParam String customerId){

        List<PaymentResponseDto> responseDtos = paymentService.getCustomerBookings(email, customerId);
        return ResponseEntity.ok(responseDtos);

    }
}
