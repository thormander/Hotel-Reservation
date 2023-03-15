package com.hotel.reservation;
import java.util.*;

public class Room{

    private int id;
    private String bedSize;
    private String smoking;
    private String amountBeds;
    private String Quality;
    private String roomInformation;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getbedSize() {
        return bedSize;
    }

    public void setbedSize(String bedSize) {
        this.bedSize = bedSize;
    }
    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }
    
    public String getamountBeds() {
        return amountBeds;
    }
    public void setamountBeds(String amountBeds) {
        this.amountBeds = amountBeds;
    }

    public void setQuality(String Quality) {
        this.Quality = Quality;
    }
    public String getQuality() {
        return Quality;
    }

   
    public String getRoomInformation() {
        return roomInformation;
    }

    public void setroomInformation(String roomInformation) {
        this.roomInformation = roomInformation;
    }
    
    public Room() {
		// TODO Auto-generated constructor stub
	}
}