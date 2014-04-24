package com.gedutech.ridesyncer.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Schedule {

	protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'h:m:ssXXX");

	long id;
	long userId;
	int weekday;
	Date start;
	Date end;
	Date deletedAt;

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("UserId", this.userId);
		obj.put("Weekday", this.weekday);
		obj.put("Start", formatDate(this.start));
		obj.put("End", formatDate(this.end));
		obj.put("Deleted", this.deletedAt);

		return obj;
	}

	public static Schedule fromJSON(JSONObject obj) throws JSONException, ParseException {
		Schedule schedule = new Schedule();

		schedule.id = obj.getLong("Id");
		schedule.userId = obj.getLong("UserId");
		schedule.weekday = obj.getInt("Weekday");
		schedule.start = parseDate(obj.getString("Start"));
		schedule.end = parseDate(obj.getString("End"));

		return schedule;
	}

	protected static Date parseDate(String dateString) throws ParseException {
		return dateFormat.parse(dateString);
	}

	protected static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

}