package com.gedutech.ridesyncer.models;

import java.util.Date;

public class Schedule {
	long id;
	long userId;
	int weekday;
	Date start;
	Date end;
	Date deletedAt;
}