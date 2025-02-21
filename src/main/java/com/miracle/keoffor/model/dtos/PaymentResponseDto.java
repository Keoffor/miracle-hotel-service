package com.miracle.keoffor.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponseDto {
    private Long paymentId;
    private String guestFullName;
    private String guestEmail;
    private String status;
    private Long roomId;
    private String roomType;
    private BigDecimal amount;
    private Date date;
}
