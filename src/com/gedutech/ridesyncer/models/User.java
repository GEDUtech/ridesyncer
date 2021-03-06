package com.gedutech.ridesyncer.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

	protected long id;
	protected String username;
	protected String password;
	protected String repeatPassword;
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String token;
	protected String ride;
	protected String address;
	protected String city;
	protected String state;
	protected String zip;
	protected double distance;
	protected boolean emailVerified;
	protected String gender;
	protected String favoriteGenre;
	protected String major;
	protected boolean smoker;
	protected boolean tos;
	protected double latitude;
	protected double longitude;

	protected List<Schedule> schedules;
	protected List<Sync> syncs;

	public User() {
		schedules = new ArrayList<>();
		syncs = new ArrayList<>();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("Username", this.username);
		obj.put("Password", this.password);
		obj.put("RepeatPassword", this.repeatPassword);
		obj.put("FirstName", this.firstName);
		obj.put("LastName", this.lastName);
		obj.put("Email", this.email);
		obj.put("EmailVerified", this.emailVerified);
		obj.put("Ride", this.ride);
		obj.put("Token", this.token);
		obj.put("Address", this.address);
		obj.put("City", this.city);
		obj.put("State", this.state);
		obj.put("Zip", this.zip);
		obj.put("FavoriteGenre", this.favoriteGenre);
		obj.put("Gender", this.gender);
		obj.put("Smoker", this.smoker);
		obj.put("Major", this.major);

		if (!Double.isNaN(this.distance)) {
			obj.put("Distance", this.distance);
		}

		JSONArray schedulesArr = new JSONArray();
		for (Schedule schedule : schedules) {
			schedulesArr.put(schedule.toJSON());
		}
		obj.put("Schedules", schedulesArr);

		JSONArray syncsArr = new JSONArray();
		for (Sync sync : syncs) {
			syncsArr.put(sync.toJSON());
		}
		obj.put("Syncs", syncsArr);
		
		obj.put("Lat", this.latitude);
		obj.put("Lng", this.longitude);

		return obj;
	}

	public static User fromJSON(JSONObject obj) throws JSONException, ParseException {
		User user = new User();

		user.id = obj.getLong("Id");
		user.username = obj.getString("Username");
		user.firstName = obj.getString("FirstName");
		user.lastName = obj.getString("LastName");
		user.email = obj.getString("Email");
		user.emailVerified = obj.getBoolean("EmailVerified");
		user.ride = obj.getString("Ride");
		user.token = obj.optString("Token");
		user.address = obj.optString("Address");
		user.city = obj.optString("City");
		user.state = obj.optString("State");
		user.zip = obj.optString("Zip");
		user.distance = obj.optDouble("Distance");
		
		user.gender = obj.getString("Gender");
		user.favoriteGenre = obj.getString("FavoriteGenre");
		user.major = obj.getString("Major");
		user.smoker = obj.getBoolean("Smoker");
		user.latitude = obj.getDouble("Lat");
		user.longitude = obj.getDouble("Lng");

		JSONArray schedulesArray = obj.optJSONArray("Schedules");
		if (schedulesArray != null) {
			for (int i = 0; i < schedulesArray.length(); i++) {
				user.schedules.add(Schedule.fromJSON(schedulesArray.getJSONObject(i)));
			}
		}

		JSONArray syncsArray = obj.optJSONArray("Syncs");
		if (syncsArray != null) {
			for (int i = 0; i < syncsArray.length(); i++) {
				user.syncs.add(Sync.fromJSON(syncsArray.getJSONObject(i)));
			}
		}

		return user;
	}

	public Schedule getScheduleOnWeekday(int weekday) {
		for (Schedule schedule : getSchedules()) {
			if (schedule.getWeekday() == weekday) {
				return schedule;

			}
		}
		return null;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public String getRide() {
		return ride;
	}

	public void setRide(String ride) {
		this.ride = ride;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String street) {
		this.address = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFavoriteGenre() {
		return favoriteGenre;
	}

	public void setFavoriteGenre(String favoriteGenre) {
		this.favoriteGenre = favoriteGenre;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public boolean isTos() {
		return tos;
	}

	public void setTos(boolean tos) {
		this.tos = tos;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> s1s) {
		this.schedules = s1s;
	}

	public List<Sync> getSyncs() {
		return syncs;
	}

	public void setSyncs(List<Sync> syncs) {
		this.syncs = syncs;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

}