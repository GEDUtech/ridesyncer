package com.gedutech.ridesyncer;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.PendingSyncsAdapter;

public class SyncReviewerActivity extends Activity {

	protected Session session;

	protected User authUser;

	PendingSyncsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_reviewer);

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		Session session = Session.getInstance(getApplicationContext());
		authUser = session.getAuthUser();

		ListView lstView = (ListView) findViewById(R.id.lst_pending_syncs);
		adapter = new PendingSyncsAdapter(this, getPendingSyncs());
		Log.d("RideSyncer", "Count: " + adapter.getCount() + " ");
		lstView.setAdapter(adapter);
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

	protected List<Sync> getPendingSyncs() {
		List<Sync> pending = new ArrayList<>();
		for (Sync sync : authUser.getSyncs()) {
			for (SyncUser syncUser : sync.getSyncUsers()) {
				if (syncUser.getStatus() == 0) {
					pending.add(sync);
					break;
				}
			}

		}
		return pending;
	}

}
