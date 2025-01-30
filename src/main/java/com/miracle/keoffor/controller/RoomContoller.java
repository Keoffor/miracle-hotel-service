package com.miracle.keoffor.controller;

import com.miracle.keoffor.exception.PhotoRetrievalException;
import com.miracle.keoffor.exception.ResourceNotFoundException;
import com.miracle.keoffor.model.BookedRoom;
import com.miracle.keoffor.model.Room;
import com.miracle.keoffor.response.BookingResponse;
import com.miracle.keoffor.response.RoomResponse;
import com.miracle.keoffor.service.BookedRoomService;
import com.miracle.keoffor.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomContoller {
    private final RoomService roomService;
    private final BookedRoomService bookedRoomService;

    @PostMapping("/add/new-room")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType")String roomType,
            @RequestParam("roomPrice")BigDecimal roomPrice
            ) throws SQLException, IOException {

          Room savedRoom = roomService.addRoom(photo,roomType,roomPrice);
          RoomResponse response = new RoomResponse(savedRoom.getId(),savedRoom.getRoomType(),
                  savedRoom.getRoomPrice());

         return ResponseEntity.ok(response);

    }
    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {
        byte[] photoByes = photo != null && !photo.isEmpty() ? photo.getBytes()
                : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoByes != null && photoByes.length > 0 ? new SerialBlob(photoByes) : null;
        Room theRoom = roomService.updateRoom(roomId,roomType,roomPrice,photoByes);
        theRoom.setPhoto(photoBlob);
        RoomResponse response = getResponse(theRoom);
        return ResponseEntity.ok(response);


    }
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
        Optional<Room> room = roomService.getRoomById(roomId);
        return room.map(rm -> {
            RoomResponse response = getResponse(rm);
            return ResponseEntity.ok(Optional.of(response));
        }).orElseThrow(() -> new ResourceNotFoundException("room not found"));
    }
    @GetMapping("/types")
    public List<String> getAllRoomTypes(){
        return roomService.getAllRoomType();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room: rooms){
            byte[] photoBytes = roomService.getRoomPhotoBytes(room.getId());
            if(photoBytes != null && photoBytes.length>0){
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse response = getResponse(room);
                response.setPhoto(base64Photo);
                roomResponses.add(response);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> roomAvailability(
            @RequestParam("checkInDate") LocalDate checkInDate,
            @RequestParam("checkOutDate")LocalDate checkOutDate,
            @RequestParam("roomType") String roomType){
        List<Room> rooms = roomService.getRoomAvailbility(checkInDate,checkOutDate,roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();

        for(Room room : rooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length > 0){
                //convert photobytes to string object
                String photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse response = getResponse(room);
                response.setPhoto(photo);
                roomResponses.add(response);
            }
        }
        if(roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.ok(roomResponses);
        }

    }

    private RoomResponse getResponse(Room room) {
        List<BookedRoom> bookedRooms = getAllBookedRoomsById(room.getId());
//        List<BookingResponse> bookingResponses = bookedRooms
//                .stream()
//                .map(bookedRoom -> new BookingResponse(bookedRoom.getBookingId(),bookedRoom.getCheckInDate(),
//                        bookedRoom.getCheckOutDate(),
//                        bookedRoom.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlo = room.getPhoto();
        if(Objects.nonNull(photoBlo)){
            try{
                photoBytes = photoBlo.getBytes(1,(int) photoBlo.length());
            }catch (SQLException sqlException){
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }

        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(),
                room.isBooked(),photoBytes);
    }

    private List<BookedRoom> getAllBookedRoomsById(Long roomId) {
        return bookedRoomService.getAllBookedRoomsByRoomId(roomId);
    }
}
