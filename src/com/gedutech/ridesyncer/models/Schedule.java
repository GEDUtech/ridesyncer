package com.gedutech.ridesyncer.models;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.gedutech.ridesyncer.utils.TimeUtil;

public class Schedule {

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
		obj.put("Start", TimeUtil.formatTime(this.start));
		obj.put("End", TimeUtil.formatTime(this.end));
		obj.put("Deleted", this.deletedAt);

		return obj;
	}

	public static Schedule fromJSON(JSONObject obj) throws JSONException, ParseException {
		Schedule schedule = new Schedule();

		schedule.id = obj.getLong("Id");
		schedule.userId = obj.getLong("UserId");
		schedule.weekday = obj.getInt("Weekday");
		schedule.start = TimeUtil.parseTime(obj.getString("Start"));
		schedule.end = TimeUtil.parseTime(obj.getString("End"));

		return schedule;
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