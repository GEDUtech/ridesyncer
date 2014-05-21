package com.gedutech.ridesyncer.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gedutech.ridesyncer.utils.TimeUtil;

public class Sync implements Comparable<Sync> {

	long id;
	Date createdAt;
	int weekday;
	long userId;
	ArrayList<SyncUser> syncUsers;

	public Sync() {
		syncUsers = new ArrayList<SyncUser>();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("Weekday", this.weekday);
		obj.put("UserId", this.userId);
		if (createdAt != null) {
			obj.put("CreatedAt", TimeUtil.formatRFC3339(createdAt));
		}

		JSONArray syncUsersArray = new JSONArray();
		for (SyncUser syncUser : syncUsers) {
			syncUsersArray.put(syncUser.toJSON());
		}
		obj.put("SyncUsers", syncUsersArray);

		return obj;
	}

	public static Sync fromJSON(JSONObject obj) throws JSONException, ParseException {
		Sync sync = new Sync();

		sync.id = obj.getLong("Id");
		sync.weekday = obj.getInt("Weekday");
		sync.userId = obj.getLong("UserId");
		sync.createdAt = TimeUtil.parseRFC3339(obj.getString("CreatedAt"));

		JSONArray syncUsersArray = obj.getJSONArray("SyncUsers");
		for (int i = 0; i < syncUsersArray.length(); i++) {
			sync.syncUsers.add(SyncUser.fromJSON(syncUsersArray.getJSONObject(i)));
		}

		return sync;
	}

	public long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public ArrayList<SyncUser> getSyncUsers() {
		return syncUsers;
	}

	public void setSyncUsers(ArrayList<SyncUser> syncUsers) {
		this.syncUsers = syncUsers;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setWeekday(long userId) {
		this.userId = userId;
	}

	@Override
	public int compareTo(Sync another) {
		if (weekday == another.weekday) {
			return 0;
		}
		return weekday > another.weekday ? 1 : -1;
	}

	public Date earlistSchedule() {
		Date earliest = null;
		for (SyncUser syncUser : syncUsers) {
			Schedule schedule = syncUser.getUser().getScheduleOnWeekday(weekday);
			if (earliest == null || schedule.getStart().before(earliest)) {
				earliest = schedule.getStart();
			}
		}
		return earliest;
	}

	public Date latestSchedule() {
		Date latest = null;
		for (SyncUser syncUser : syncUsers) {
			Schedule schedule = syncUser.getUser().getScheduleOnWeekday(weekday);
			if (latest == null || schedule.getEnd().after(latest)) {
				latest = schedule.getEnd();
			}
		}
		return latest;
	}

	public List<SyncUser> getDrivers() {
		List<SyncUser> drivers = new ArrayList<>();
		for (SyncUser syncUser : syncUsers) {
			if (syncUser.getOrder() > 0) {
				drivers.add(syncUser);
			}
		}
		return drivers;
	}
	
	public HashSet<Long> getSyncUsersIdHashSet() {
		List<Long> ids = new ArrayList<>(syncUsers.size());
		for (SyncUser syncUser : syncUsers) {
			ids.add(syncUser.getUserId());
		}
		Collections.sort(ids);
		return new HashSet<Long>(ids);
	}

}