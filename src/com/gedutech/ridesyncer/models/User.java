package com.gedutech.ridesyncer.models;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public long id;
	String username;
	String password;
	String repeatPassword;
	String firstName;
	String lastName;
	String email;
	String token;
	String ride;
	String address;
	String city;
	String state;
	String zip;
	double distance;
	boolean emailVerified;
	boolean tos;

	List<Schedule> schedules;

	public User() {
		schedules = new ArrayList<Schedule>();
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

		if (!Double.isNaN(this.distance)) {
			obj.put("Distance", this.distance);
		}

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

		JSONArray schedulesArray = obj.optJSONArray("Schedules");
		if (schedulesArray != null) {
			for (int i = 0; i < schedulesArray.length(); i++) {
				user.schedules.add(Schedule.fromJSON(schedulesArray.getJSONObject(i)));
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

}