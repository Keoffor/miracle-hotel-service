package com.miracle.keoffor.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomDto {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
}
