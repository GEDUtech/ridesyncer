package com.gedutech.ridesyncer.models;

import java.util.Date;
import java.util.ArrayList;

public class Sync {
	long id;
	Date createdAt;
	ArrayList<SyncUser> syncUsers;
	ArrayList<SyncDay> syncDays;
	
	public Sync() {
		syncUsers = new ArrayList<SyncUser>();
		syncDays = new ArrayList<SyncDay>();
	}
}