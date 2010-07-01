package com.repeatermap.repeatermap;

//this is the parsed structure of a point
public class RepeaterPoint {

	private String name = null;
	private String description = null;
	private Double latitude = null;
	private Double longitude = null;
	
	public RepeaterPoint() {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public void setLatitude(double lat) {
		this.latitude = lat;
	}
	
	public void setLongitude(double lon) {
		this.longitude = lon;
	}
	
}
