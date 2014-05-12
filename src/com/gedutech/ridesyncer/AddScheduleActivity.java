package com.gedutech.ridesyncer;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.SchedulesApi;
import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.User;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddScheduleActivity extends Activity {

	protected Session session;
	protected User authUser;
	protected SchedulesApi schedulesApi;
	protected AddScheduleTask mAddScheduleTask;

	private int daySelection;
	private int hour, min;
	private Schedule schedule;

	private EditText etfStartTime, etfEndTime;
	private Spinner spinnerWeekday;
	List<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_add);
		spinnerWeekday = (Spinner) findViewById(R.id.spinnerWeekday);
		etfStartTime = (EditText) findViewById(R.id.etfStartTime);
		etfEndTime = (EditText) findViewById(R.id.etfEndTime);
		daySelection = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		hour = Calendar.getInstance().get(Calendar.HOUR);
		min = Calendar.getInstance().get(Calendar.MINUTE);

		session = Session.getInstance(getApplicationContext());
		authUser = session.getAuthUser();
		schedulesApi = new SchedulesApi(authUser.getToken());
		schedule = new Schedule();

		String[] weekdays = new DateFormatSymbols().getWeekdays();
		list = new ArrayList<String>();

		for (String s : weekdays) {
			list.add(s);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerWeekday.setAdapter(adapter);

		spinnerWeekday.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				daySelection = pos;
				schedule.setWeekday(daySelection);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		etfStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog timePicker = new TimePickerDialog(AddScheduleActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								hour = hourOfDay;
								min = minute;
								String meridian = hourOfDay >= 12 ? "PM" : "AM";
								if (hourOfDay > 12)
									hourOfDay -= 12;
								etfStartTime.setText(String.format("%02d:%02d%s", hourOfDay, minute, meridian));

								Date startDate = new Date();
								startDate.setHours(hour);
								startDate.setMinutes(min);
								schedule.setStart(startDate);
							}
						}, hour, min, false);

				timePicker.setTitle("Select Start Time");
				timePicker.show();
			}
		});

		etfEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog timePicker = new TimePickerDialog(AddScheduleActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
								hour = hourOfDay;
								min = minute;
								String meridian = hourOfDay >= 12 ? "PM" : "AM";
								if (hourOfDay > 12)
									hourOfDay -= 12;
								etfEndTime.setText(String.format("%02d:%02d%s", hourOfDay, minute, meridian));
								Date endDate = new Date();
								endDate.setHours(hour);
								endDate.setMinutes(min);
								schedule.setEnd(endDate);
							}
						}, hour, min, false);

				timePicker.setTitle("Select End Time");
				timePicker.show();
			}
		});

		findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (schedule.getStart().before(schedule.getEnd())) {
					attemptAddSchedule();
				} else
					Toast.makeText(getApplicationContext(), "Start time must be before end time", Toast.LENGTH_LONG)
							.show();
			}
		});

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void attemptAddSchedule() {
		if (mAddScheduleTask != null) {
			return;
		}
		try {
			mAddScheduleTask = new AddScheduleTask();
			mAddScheduleTask.execute();
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	protected class AddScheduleTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... params) {
			ApiResult result = null;
			try {
				result = schedulesApi.add(schedule);
			} catch (JSONException e) {
				e.printStackTrace();
				cancel(true);
			}
			return result;
		}

		@Override
		protected void onPostExecute(ApiResult result) {
			super.onPostExecute(result);

			if (result.isSuccess()) {
				authUser.getSchedules().add(schedule);
				session.saveAuthUser();
				Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
				Log.d("Ridesyncer", result.getRaw());
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mAddScheduleTask = null;
		}
	}
}
