package com.gedutech.ridesyncer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.SyncsApi;
import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.views.SyncEditorCell;
import com.gedutech.ridesyncer.widgets.ProgressSwitcher;

public class SyncEditorActivity extends Activity {

	protected Session session;
	protected User authUser;
	protected List<User> others;
	protected ViewGroup syncEditorTable;
	protected SyncManager syncManager;
	protected SyncsApi syncsApi;
	protected ProgressSwitcher progressSwitcher;

	protected CreateSyncTask mCreateSyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_editor);

		progressSwitcher = new ProgressSwitcher(findViewById(R.id.syncRequestStatus), findViewById(R.id.syncEditorForm));
		syncEditorTable = (ViewGroup) findViewById(R.id.sync_editor_table);

		findViewById(R.id.btnRequestSync).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptCreateSync();
			}
		});

		session = Session.getInstance(getApplicationContext());
		authUser = session.getAuthUser();
		syncsApi = new SyncsApi(authUser.getToken());

		others = new ArrayList<>();

		for (long id : getIntent().getExtras().getLongArray("ids")) {
			for (User user : session.getMatches()) {
				if (user.getId() == id) {
					others.add(user);
					break;
				}
			}
		}

		syncManager = new SyncManager(authUser, others);
		makeHeader();
		makeTable();
	}

	protected void attemptCreateSync() {
		if (mCreateSyncTask != null) {
			return;
		}

		try {
			syncManager.validate();
			mCreateSyncTask = new CreateSyncTask();
			mCreateSyncTask.execute();
			progressSwitcher.showProgress(true);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	protected void makeHeader() {
		ViewGroup header = (ViewGroup) findViewById(R.id.syncEditorHeader);
		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,
				1f);

		for (String title : syncManager.getHeaders()) {
			TextView txtWeekday = new TextView(this);
			txtWeekday.setLayoutParams(params);
			txtWeekday.setText(title);
			txtWeekday.setTextColor(getResources().getColor(R.color.dark));
			txtWeekday.setTypeface(null, Typeface.BOLD);
			txtWeekday.setGravity(Gravity.CENTER);
			header.addView(txtWeekday);
		}
	}

	Map<Integer, List<SyncEditorCell>> cells;

	protected void makeTable() {
		List<User> users = syncManager.getAllUsers();
		List<Integer> weekdays = syncManager.getWeekdays();

		cells = new HashMap<>();
		for (int weekday : weekdays) {
			cells.put(weekday, new ArrayList<SyncEditorCell>());
		}

		for (User user : users) {
			TableRow row = new TableRow(this);
			row.setPadding(10, 10, 10, 10);
			row.setMinimumHeight(100);

			TextView txtUsername = new TextView(this);
			txtUsername.setText(user.getUsername());
			LinearLayout.LayoutParams params = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
			params.gravity = Gravity.CENTER;

			txtUsername.setLayoutParams(params);
			txtUsername.setGravity(Gravity.CENTER);
			txtUsername.setTextColor(this.getResources().getColor(R.color.dark));
			txtUsername.setTypeface(null, Typeface.BOLD);
			row.addView(txtUsername);

			for (final int weekday : weekdays) {
				Schedule schedule = user.getScheduleOnWeekday(weekday);
				View view;

				if (schedule == null) {
					view = new View(this);
				} else {
					final SyncUser syncUser = syncManager.getSyncUserForSchedule(schedule);
					SyncEditorCell cell = new SyncEditorCell(this, syncManager, weekday, syncUser);
					cell.setOnCellStateChangedListener(new SyncEditorCell.CellStateChangedListener() {
						@Override
						public void onStateChanged(SyncEditorCell cell) {
							Log.d("RideSyncer", "Checked.. updating....");
							for (SyncEditorCell otherCell : cells.get(weekday)) {
								otherCell.update();
							}
						}
					});

					view = cell;
					cells.get(weekday).add(cell);
				}
				view.setLayoutParams(params);
				row.addView(view);
			}

			syncEditorTable.addView(row);
		}
	}

	protected class CreateSyncTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... params) {
			ApiResult result = null;
			try {
				result = syncsApi.create(new ArrayList<Sync>(syncManager.getSync().values()));
			} catch (JSONException e) {
				progressSwitcher.showProgress(false);
				cancel(true);
			}
			return result;
		}

		@Override
		protected void onPostExecute(ApiResult result) {
			super.onPostExecute(result);
			mCreateSyncTask = null;
			progressSwitcher.showProgress(false);

			if (result.isSuccess()) {
				try {
					JSONArray resultsArr = result.getData().getJSONArray("results");

					List<Sync> syncs = new ArrayList<>(resultsArr.length());
					for (int i = 0; i < resultsArr.length(); i++) {
						syncs.add(Sync.fromJSON(resultsArr.getJSONObject(i)));
					}

					authUser.setSyncs(syncs);
					session.saveAuthUser();

					Toast.makeText(getApplicationContext(), "Sync request has been sent", Toast.LENGTH_LONG).show();
					finish();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			mCreateSyncTask = null;
			progressSwitcher.showProgress(false);
		}

	}
}
