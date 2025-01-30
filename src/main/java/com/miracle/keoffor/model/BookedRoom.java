package com.miracle.keoffor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @Column (name = "check_in")
    private LocalDate checkInDate;
    @Column (name = "check_out")
    private LocalDate checkOutDate;
    @Column (name = "guest_fullname")
    private String guestFullName;
    @Column (name = "guest_email")
    private  String guestEmail;
    @Column (name = "adults")
    private int numOfAdults;
    @Column (name = "children")
    private  int numOfChildren;
    @Column (name = "total_number_guest")
    private  int totalNumOfGuest;
    @Column (name = "confirmation_code")
    private String bookingConfirmationCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "room_id")
    private Room room;

    private void calculateNumOfGuests(){
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookedRoom that)) return false;
        return Objects.equals(getGuestEmail(), that.getGuestEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGuestEmail());
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateNumOfGuests();
    }

    public void setNumOfChilderen(int numOfChilderen) {
        this.numOfChildren = numOfChilderen;
        calculateNumOfGuests();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
