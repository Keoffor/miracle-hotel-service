package com.miracle.keoffor.service.custom;

import com.miracle.keoffor.config.client.ClientService;
import com.miracle.keoffor.exception.ResourceNotFoundException;
import com.miracle.keoffor.model.User;
import com.miracle.keoffor.model.dtos.PaymentResponseDto;
import com.miracle.keoffor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PaymentService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private  final ClientService clientService;


    public List<PaymentResponseDto> getCustomerBookings(String email, String customerId) {
        User user = userRepository.findByIdAndEmail(Long.parseLong(customerId), email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with email %s and ID %s not found", email, customerId)
                ));

        return clientService.getCustomerBookings(user.getEmail(), String.valueOf(user.getId()));
    }

}
