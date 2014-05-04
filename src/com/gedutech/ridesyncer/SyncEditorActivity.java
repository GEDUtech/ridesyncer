package com.gedutech.ridesyncer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.SyncsApi;
import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.ProgressSwitcher;
import com.gedutech.ridesyncer.widgets.SyncEditorAdapter;

public class SyncEditorActivity extends Activity {

	protected Session session;
	protected User authUser;
	protected List<User> users;
	protected ListView lstSyncEditor;
	protected SyncManager syncManager;
	protected SyncsApi syncsApi;
	protected ProgressSwitcher progressSwitcher;

	protected CreateSyncTask mCreateSyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_editor);

		progressSwitcher = new ProgressSwitcher(findViewById(R.id.syncRequestStatus), findViewById(R.id.syncEditorForm));

		lstSyncEditor = (ListView) findViewById(R.id.lstSyncEditor);
		findViewById(R.id.btnRequestSync).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptCreateSync();
			}
		});

		session = Session.getInstance(getApplicationContext());
		authUser = session.getAuthUser();
		syncsApi = new SyncsApi(authUser.getToken());

		// TODO: Remove
		if (authUser.getSchedules().size() == 0) {
			authUser.getSchedules().add(make(authUser, 1));
			authUser.getSchedules().add(make(authUser, 2));
			authUser.getSchedules().add(make(authUser, 3));
			authUser.getSchedules().add(make(authUser, 4));
			authUser.getSchedules().add(make(authUser, 5));
		}

		users = new ArrayList<>();

		// u1
		User u1 = new User();
		u1.id = 2;
		u1.setUsername("BurpTurp");
		u1.getSchedules().add(make(u1, 1));
		u1.getSchedules().add(make(u1, 3));

		// u2
		User u2 = new User();
		u2.id = 3;
		u2.setUsername("ChopStop");
		u2.getSchedules().add(make(u2, 1));
		// u2.getSchedules().add(make(u2, 2));
		u2.getSchedules().add(make(u2, 3));
		// u2.getSchedules().add(make(u2, 4));
		u2.getSchedules().add(make(u2, 5));

		users.add(u1);
		users.add(u2);
		// END REMOVE

		syncManager = new SyncManager(authUser, users);

		List<User> objects = new ArrayList<>();
		objects.add(authUser);
		objects.addAll(users);

		SyncEditorAdapter adapter = new SyncEditorAdapter(this, syncManager, objects);
		lstSyncEditor.setAdapter(adapter);

		makeHeader();
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

	// TODO: Remove
	protected Schedule make(User user, int weekday) {
		Schedule s01 = new Schedule();
		s01.setStart(new Date(0, 0, 0, 12, 0));
		s01.setEnd(new Date(0, 0, 0, 1, 0));
		s01.setWeekday(weekday);
		s01.setUserId(user.getId());
		return s01;
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

			// session.write(Sesion, data)

			if (result.isSuccess()) {
				Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
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
