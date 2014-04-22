package com.gedutech.ridesyncer.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    long id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String token;
    String ride;
    boolean emailVerified;
    String verificationCode;
    ArrayList<Schedule> schedules;

    public User() {
        schedules = new ArrayList<Schedule>();
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();

        obj.put("Username", this.username);
        obj.put("Password", this.password);
        obj.put("FirstName", this.firstName);
        obj.put("LastName", this.lastName);
        obj.put("Email", this.email);
        obj.put("EmailVerified", this.emailVerified);
        obj.put("VerificationCode", this.verificationCode);
        obj.put("Ride", this.ride);
        obj.put("Token", this.token);
//	  obj.put("CreatedAt", this.created);
        return obj;
    }

    public static User fromJSON(JSONObject obj) throws JSONException {
        User user = new User();

        user.id = obj.getLong("Id");
        user.username = obj.getString("UserName");
        user.firstName = obj.getString("FirstName");
        user.lastName = obj.getString("LastName");
        user.email = obj.getString("Email");
        user.emailVerified = obj.getBoolean("EmailVerified");
        user.verificationCode = obj.getString("VerificationCode");
        user.ride = obj.getString("Ride");
        user.token = obj.getString("Token");
//        user.created = obj.getString("Created");
        return user;

    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setToken(String token) {
		this.token = token;
	}

	public String getRide() {
		return ride;
	}

	public void setRide(String ride) {
		this.ride = ride;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(ArrayList<Schedule> schedules) {
		this.schedules = schedules;
	}

}