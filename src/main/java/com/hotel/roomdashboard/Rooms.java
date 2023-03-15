package com.hotel.roomdashboard;

public class Rooms {
	
	private int id;
	private String bedSize;
	private String smoking;
	private String amountBeds;
	private String quality;
	private String roomInformation;
	
	public Rooms(String bedSize, String smoking, String amountBeds, String quality, String roomInformation) {
		super();
		this.bedSize = bedSize;
		this.smoking = smoking;
		this.amountBeds = amountBeds;
		this.quality = quality;
		this.roomInformation = roomInformation;
	}

	public Rooms(int id, String bedSize, String smoking, String amountBeds, String quality,
			String roomInformation) {
		super();
		this.id = id;
		this.bedSize = bedSize;
		this.smoking = smoking;
		this.amountBeds = amountBeds;
		this.quality = quality;
		this.roomInformation = roomInformation;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBedSize() {
		return bedSize;
	}
	public void setBedSize(String bedSize) {
		this.bedSize = bedSize;
	}
	public String getSmoking() {
		return smoking;
	}
	public void setSmoking(String smoking) {
		this.smoking = smoking;
	}
	public String getAmountBeds() {
		return amountBeds;
	}
	public void setAmountBeds(String amountBeds) {
		this.amountBeds = amountBeds;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getRoomInformation() {
		return roomInformation;
	}
	public void setRoomInformation(String roomInformation) {
		this.roomInformation = roomInformation;
	}

}
