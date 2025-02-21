package com.miracle.keoffor.config.client;

import com.miracle.keoffor.model.dtos.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
public class ClientService {

    @Autowired
    WebClient webClient;


    public List<PaymentResponseDto> getCustomerBookings(String email, String customerId){

        return webClient.get()
                .uri(urlBuilder -> urlBuilder
                        .path("/customer-bookings")
                        .queryParam("email", email)
                        .queryParam("customerId",customerId)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Client error: " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Server error: " + clientResponse.statusCode())))
                .bodyToFlux(PaymentResponseDto.class)
                .collectList()
                .block();
    }
}
