package com.gedutech.ridesyncer.models;

import java.util.Date;

public class SyncDay {
	long id;
	long driverId;
	long passengerId;
	long syncId;
	int weekday;
	int status;
	int order;
	Date createdAt;
}