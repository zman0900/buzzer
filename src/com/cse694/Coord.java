package com.cse694;

public class Coord {
	private double latitude;
	private double longitude;
	
	public Coord (double lati, double longi) {
		this.latitude = lati;
		this.longitude = longi;
	}
	
	public double get_long () {
		return this.longitude;
	}
	
	public double get_lat () {
		return this.latitude;
	}
}
