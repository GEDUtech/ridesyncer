package com.gedutech.ridesyncer.models;

import java.util.ArrayList;

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
}