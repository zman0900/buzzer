package com.cse694;
import java.lang.*;
import java.util.*;

public class Restaurant {
	private String name = "";
	private Prices price_range = Prices.NONE;
	private Foods food_type = Foods.NONE;
	private Coord coordinates = null;
	private ArrayList<User> user_list = null;
	
	public Restaurant () {
		user_list = new ArrayList<User>();
	}
	
	public boolean register (String name, Prices price_range, Foods food_type, Coord coordinates) {
		this.name = name;
		this.price_range = price_range;
		this.food_type = food_type;
		this.coordinates = coordinates;
		return true;
	}

	public boolean acceptCheckIn (User user) {
		if (!user_list.contains(user)) {
			user_list.add(user);
			return true;
		}
		return false;
	}
	
	
	
}
