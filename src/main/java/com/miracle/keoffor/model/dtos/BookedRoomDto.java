package com.miracle.keoffor.model.dtos;

import com.miracle.keoffor.model.BookedRoom;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookedRoomDto {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName;
    private String guestEmail;
    private String bookingConfirmationCode;
    private RoomDto room;

    public static BookedRoomDto convertToDto(BookedRoom bookedRoom){
         RoomDto room = new RoomDto(bookedRoom.getRoom().getId(),
                 bookedRoom.getRoom().getRoomType(),bookedRoom.getRoom().getRoomPrice());
        return BookedRoomDto.builder()
        .bookingId(bookedRoom.getBookingId())
        .checkInDate(bookedRoom.getCheckInDate())
        .checkOutDate(bookedRoom.getCheckOutDate())
        .guestFullName(bookedRoom.getGuestFullName())
        .bookingConfirmationCode(bookedRoom.getBookingConfirmationCode())
        .guestEmail(bookedRoom.getGuestEmail())
        .room(room).build();

    }
}
