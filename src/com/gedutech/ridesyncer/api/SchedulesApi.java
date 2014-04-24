package com.gedutech.ridesyncer.api;

import org.json.JSONException;

import com.gedutech.ridesyncer.models.Schedule;

public class SchedulesApi extends ApiBase {

	public SchedulesApi(String token) {
		super("schedules", token);
	}

	public ApiResult add(Schedule schedule) throws JSONException {
		return execute(post("/add"), schedule.toJSON());
	}

}
