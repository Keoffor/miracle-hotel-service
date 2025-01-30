package com.miracle.keoffor.service;

import com.miracle.keoffor.model.BookedRoom;
import com.miracle.keoffor.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room addRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException;

    List<String> getAllRoomType();

    List<Room> getAllRooms();

    byte[] getRoomPhotoBytes(Long roomId) throws SQLException;

    void deleteRoom(Long roomId);

    byte[] getRoomPhotoByRoomId(Long roomId);

    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoByes);

    Optional<Room> getRoomById(Long roomId);

    List<Room> getRoomAvailbility(LocalDate checkInDate, LocalDate checkOutDate, String roomType);


}
