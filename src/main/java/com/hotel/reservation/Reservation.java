package com.hotel.reservation;
import java.util.*;

public class Reservation{

    private String id;
    private String startDate;
    private String endDate;
    private String reservationName;
    private String accountType;
    private String roomInformation;
    private String roomId;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getRoomInformation() {
        return roomInformation;
    }

    public void setRoomInformation(String roomInformation) {
        this.roomInformation = roomInformation;
    }

    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
    	this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }
    
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    
    public Reservation() {}

}
