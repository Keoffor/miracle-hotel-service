package com.miracle.keoffor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.miracle.keoffor.constants.RoomConstants.CHARACTERS;

@AllArgsConstructor
@Setter
@Getter
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private static final Random RANDOM = new Random();
    private boolean isBooked = false;
    @Lob
    private Blob photo;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<BookedRoom> bookings;

    public Room(){
        this.bookings = new ArrayList<>();
    }


    private static String generateRandomString() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i <10; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    public void addBooking (BookedRoom booking){
        if (booking == null){
            this.bookings = new ArrayList<>();
        }
        this.bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        String bookingCode= generateRandomString();
        booking.setBookingConfirmationCode(bookingCode);
    }
}
