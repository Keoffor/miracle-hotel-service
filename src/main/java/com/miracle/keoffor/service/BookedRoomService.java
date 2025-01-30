package com.miracle.keoffor.service;

import com.miracle.keoffor.model.BookedRoom;
import com.miracle.keoffor.model.dtos.BookedRoomDto;

import java.util.List;


public interface BookedRoomService {
    List<BookedRoom> getAllBookedRoomsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();

    BookedRoom findByConfirmationCode(String confirmationCode);

    String savingBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);

    List<BookedRoomDto> getUserBookedRooms(String email);
}
