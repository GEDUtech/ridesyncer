package com.gedutech.ridesyncer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncGroup;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.PendingSyncsAdapter;
import com.gedutech.ridesyncer.widgets.ReviewSyncGroupAdapter;

public class SyncReviewerActivity extends Activity {

	protected Session session;

	protected User authUser;

	PendingSyncsAdapter adapter;
	ReviewSyncGroupAdapter adapter2;

	Button btnAccept, btnDecline;

	View v1, v2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_reviewer);

		v1 = findViewById(R.id.pending);
		v2 = findViewById(R.id.appdis);

		btnAccept = (Button) findViewById(R.id.btn_accept);
		btnDecline = (Button) findViewById(R.id.btn_decline);

		btnAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		Session session = Session.getInstance(getApplicationContext());
		authUser = session.getAuthUser();

		ListView lstView = (ListView) findViewById(R.id.lst_pending_syncs);
		final ExpandableListView lstView2 = (ExpandableListView) findViewById(R.id.lst_approve_syncs);
		adapter = new PendingSyncsAdapter(this, getSyncGroups());

		lstView.setAdapter(adapter);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				v1.setVisibility(View.GONE);
				v2.setVisibility(View.VISIBLE);

				SyncGroup group = adapter.getItem(position);
				lstView2.setAdapter(new ReviewSyncGroupAdapter(SyncReviewerActivity.this, group));

				for (int i = 0; i < group.syncs.size(); i++) {
					lstView2.expandGroup(i);
				}
			}
		});

	}

	protected List<SyncGroup> getSyncGroups() {
		List<SyncGroup> groups = new ArrayList<>();
		List<Sync> pendingSyncs = getPendingSyncs();
		HashMap<Integer, SyncGroup> map = new HashMap<>();

		for (Sync sync : pendingSyncs) {
			HashSet<Long> idSet = sync.getSyncUsersIdHashSet();
			SyncGroup group = map.get(idSet.hashCode());

			if (group == null) {
				group = new SyncGroup();
				map.put(idSet.hashCode(), group);
			}

			group.syncs.add(sync);
		}

		for (Integer key : map.keySet()) {
			groups.add(map.get(key));
		}

		return groups;
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

	@Override
	public void onBackPressed() {
		if (v2.getVisibility() == View.VISIBLE) {
			v1.setVisibility(View.VISIBLE);
			v2.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
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

}
