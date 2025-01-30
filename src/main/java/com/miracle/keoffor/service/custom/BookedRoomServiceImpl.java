package com.miracle.keoffor.service.custom;

import com.miracle.keoffor.exception.InvalidCodeBookingRequestException;
import com.miracle.keoffor.exception.ResourceNotFoundException;
import com.miracle.keoffor.model.BookedRoom;
import com.miracle.keoffor.model.Room;
import com.miracle.keoffor.model.dtos.BookedRoomDto;
import com.miracle.keoffor.repository.BookedRoomRespository;
import com.miracle.keoffor.service.BookedRoomService;
import com.miracle.keoffor.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookedRoomServiceImpl implements BookedRoomService {
    private final BookedRoomRespository bookedRoomRespository;
    private final RoomService roomService;
    @Override
    public List<BookedRoom> getAllBookedRoomsByRoomId(Long roomId) {

        return bookedRoomRespository.findByRoomId(roomId);
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookedRoomRespository.findAll();
    }

    @Override
    public BookedRoom findByConfirmationCode(String confirmationCode) {
        return bookedRoomRespository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with confirmation code : "
                        +confirmationCode));
    }

    @Override
    public String savingBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidCodeBookingRequestException("Check-in-Date must be before Check-out-Date");
        }

        Room theRoom = roomService.getRoomById(roomId).get();
        List<BookedRoom>existingBookings = theRoom.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if(roomIsAvailable){
            theRoom.addBooking(bookingRequest);
            bookedRoomRespository.save(bookingRequest);

        }else{
            throw  new InvalidCodeBookingRequestException("Sorry...This room is not available for the selected dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookedRoomRespository.deleteById(bookingId);
    }

    @Override
    public List<BookedRoomDto> getUserBookedRooms(String email) {
        List<BookedRoom> response = bookedRoomRespository.findByGuestEmail(email).stream().toList();
          List<BookedRoomDto> result = response.stream().map(BookedRoomDto::convertToDto).toList();
        if(result.isEmpty()){
            throw new UsernameNotFoundException("User name is not found");
        }
        return result;
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                    bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())||
                    bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()) ||
                    bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()) &&
                    bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()) ||
                    bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) &&
                    bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()) ||
                    bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) &&
                    bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()) ||
                    bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) &&
                    bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())||
                    bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) &&
                    bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                );
    }
}
