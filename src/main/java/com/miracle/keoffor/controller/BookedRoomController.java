package com.miracle.keoffor.controller;

import com.miracle.keoffor.exception.InvalidCodeBookingRequestException;
import com.miracle.keoffor.exception.ResourceNotFoundException;
import com.miracle.keoffor.model.BookedRoom;
import com.miracle.keoffor.model.Room;
import com.miracle.keoffor.model.dtos.BookedRoomDto;
import com.miracle.keoffor.response.BookingResponse;
import com.miracle.keoffor.response.RoomResponse;
import com.miracle.keoffor.service.BookedRoomService;
import com.miracle.keoffor.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookedRoomController {
    private final BookedRoomService bookedRoomService;
    private final RoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getBookings(){
        List<BookedRoom> bookings = bookedRoomService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom roomBooked: bookings){
            BookingResponse bookingResponse = getBookingResponse(roomBooked);
             bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }
    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookedRoom bookedRoom = bookedRoomService.findByConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(bookedRoom);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookingRequest){
        try{
            String confirmationCode = bookedRoomService.savingBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room booking was successful.Your booking confirmation code is : "+ confirmationCode);
        }catch (InvalidCodeBookingRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());

        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookedRoomService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedRoom bookedRoom) {
        Room theRoom = roomService.getRoomById(bookedRoom.getRoom().getId()).get();
        RoomResponse room = new RoomResponse (theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return new BookingResponse(bookedRoom.getBookingId(),
                bookedRoom.getCheckInDate(),bookedRoom.getCheckOutDate(),
                bookedRoom.getGuestFullName(),bookedRoom.getGuestEmail(),
                bookedRoom.getNumOfAdults(), bookedRoom.getNumOfChildren(),
                bookedRoom.getTotalNumOfGuest(),bookedRoom.getBookingConfirmationCode(),
                room);
    }

    @GetMapping("/{email}/booking")
    public ResponseEntity<?> getBookedRoomsByEmail(@PathVariable String email){
        try {
            List<BookedRoomDto> userBookedRooms = bookedRoomService.getUserBookedRooms(email);
            return new ResponseEntity<List<BookedRoomDto>>(userBookedRooms, HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
