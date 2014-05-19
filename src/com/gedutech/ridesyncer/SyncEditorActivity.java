package com.gedutech.ridesyncer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
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
import com.gedutech.ridesyncer.widgets.SyncEditorAdapter;

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

		// progressSwitcher = new
		// ProgressSwitcher(findViewById(R.id.syncRequestStatus),
		// findViewById(R.id.syncEditorForm));
		// syncEditorTable = (ViewGroup) findViewById(R.id.sync_editor_table);
		//
		// findViewById(R.id.btnRequestSync).setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// attemptCreateSync();
		// }
		// });
		//
		session = Session.getInstance(getApplicationContext());
		authUser = session.getAuthUser();
		syncsApi = new SyncsApi(authUser.getToken());

		others = new ArrayList<>();

		if (getIntent().getExtras().getBoolean("edit", false)) {
			Set<Long> idSet = new HashSet<>();
			for (Sync sync : authUser.getSyncs()) {
				for (SyncUser syncUser : sync.getSyncUsers()) {
					if (syncUser.getUserId() != authUser.getId()) {
						if (!idSet.contains(syncUser.getUserId())) {
							others.add(syncUser.getUser());
							idSet.add(syncUser.getUserId());
						}
					}
				}
			}
		} else {
			for (long id : getIntent().getExtras().getLongArray("ids")) {
				for (User user : session.getMatches()) {
					if (user.getId() == id) {
						others.add(user);
						break;
					}
				}
			}
		}
		//
		syncManager = new SyncManager(authUser, others);

		ListView lstView = (ListView) findViewById(R.id.lst_sync_editor);
		
		List<Sync> syncs = new ArrayList<>(syncManager.getSyncs().size());
		for (Integer key : syncManager.getSyncs().keySet()) {
			syncs.add(syncManager.getSyncs().get(key));
		}
		SyncEditorAdapter adapter = new SyncEditorAdapter(this, syncs, authUser);

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(ab.getThemedContext(),
				R.array.sync_editor_action_list, android.R.layout.simple_spinner_dropdown_item);

		ab.setListNavigationCallbacks(mSpinnerAdapter, new ActionBar.OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {

				return false;
			}
		});
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

	protected class CreateSyncTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... params) {
			ApiResult result = null;
			try {
				result = syncsApi.create(new ArrayList<Sync>(syncManager.getSyncs().values()));
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

			Log.d("RideSyncer", result.toString());

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
