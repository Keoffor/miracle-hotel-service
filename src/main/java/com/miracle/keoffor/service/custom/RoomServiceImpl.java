package com.miracle.keoffor.service.custom;

import com.miracle.keoffor.exception.InternalServerException;
import com.miracle.keoffor.exception.ResourceNotFoundException;
import com.miracle.keoffor.model.Room;
import com.miracle.keoffor.repository.RoomRespository;
import com.miracle.keoffor.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRespository roomRespository;
    @Override
    public Room addRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {

        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(!file.isEmpty()){
            byte[] photoByes = file.getBytes();
            Blob blob = new SerialBlob(photoByes);
            room.setPhoto(blob);
        }
        return roomRespository.save(room);
    }

    @Override
    public List<String> getAllRoomType() {
        return roomRespository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRespository.findAll();
    }

    @Override
    public byte[] getRoomPhotoBytes(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomRespository.findById(roomId);
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Room not found");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(Objects.nonNull(photoBlob)){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRespository.findById(roomId);
        if(theRoom.isPresent()){
            roomRespository.deleteById(roomId);
        }
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        byte[] photoBytes = null;
        Optional<Room> room = roomRespository.findById(roomId);
         if(room.isPresent()){
             try{
                 photoBytes = room.get().getPhoto() !=null
                         ? room.get().getPhoto().getBytes(1L, (int) room.get().getPhoto().length())
                         : null;
             }catch (SQLException e){
                 throw new InternalServerException(" not found");
             }
         }
        return photoBytes;
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoByes) {
        Room room = roomRespository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException(" resource not found"));
        if(roomType != null) room.setRoomType(roomType);
        if(roomPrice!=null) room.setRoomPrice(roomPrice);
        if(photoByes !=null && photoByes.length > 0){
            try{
              room.setPhoto( new SerialBlob(photoByes));
            }catch (SQLException ex){
             throw new InternalServerException("Error updating photo");
            }
        }
        return roomRespository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRespository.findById(roomId).get());
    }

    @Override
    public List<Room> getRoomAvailbility(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRespository.findAvailableRoomsByDateAndType(checkInDate,checkOutDate,roomType);
    }
}
