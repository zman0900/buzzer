package com.cse694;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Restaurant {

	private static Map<Integer, Restaurant> restaurants = null;

	private int id;
	private String name;
	private String description;
	private int latitude;
	private int longitude;
	private List<User> user_list = null;

	public Restaurant() {
		user_list = new ArrayList<User>();
	}

	/*
	 * public boolean register(String name, Prices price_range, Foods food_type,
	 * Coord coordinates) { this.name = name; this.price_range = price_range;
	 * this.food_type = food_type; this.coordinates = coordinates; return true;
	 * }
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public boolean acceptCheckIn(User user) {
		if (!user_list.contains(user)) {
			user_list.add(user);
			return true;
		}
		return false;
	}

	public static Restaurant getRestaurantById(Context ctx, int id) {
		if (restaurants == null) {
			try {
				loadRestaurants(ctx);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return restaurants.get(id);
	}
	
	public static List<Restaurant> getAllRestaurants(Context ctx) {
		if (restaurants == null) {
			try {
				loadRestaurants(ctx);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return new ArrayList<Restaurant>(restaurants.values());
	}

	private static void loadRestaurants(Context ctx) throws IOException {
		InputStream inputStream = ctx.getResources().openRawResource(
				R.raw.restaurants);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));
		StringWriter writer = new StringWriter();
		String buffer = null;
		while ((buffer = br.readLine()) != null) {
			writer.append(buffer);
		}
		try {
			JSONObject jsonObject = new JSONObject(writer.toString());
			JSONArray allRest = jsonObject.getJSONArray("restaurants");
			restaurants = new HashMap<Integer, Restaurant>(allRest.length());
			for (int i = 0; i < allRest.length(); i++) {
				JSONObject jrest = allRest.getJSONObject(i);
				jrest = jrest.getJSONObject("restaurant");
				// Build restaurant
				Restaurant rest = new Restaurant();
				rest.setId(jrest.getInt("id"));
				rest.setName(jrest.getString("name"));
				rest.setDescription(jrest.getString("desc"));
				rest.setLatitude(jrest.getInt("lat"));
				rest.setLongitude(jrest.getInt("lon"));
				restaurants.put(rest.getId(), rest);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", description="
				+ description + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

}
