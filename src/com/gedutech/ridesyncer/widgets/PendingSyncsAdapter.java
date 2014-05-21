package com.gedutech.ridesyncer.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncGroup;
import com.gedutech.ridesyncer.utils.TimeUtil;

public class PendingSyncsAdapter extends ArrayAdapter<SyncGroup> {

	public PendingSyncsAdapter(Context context, List<SyncGroup> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalater.inflate(R.layout.pending_syncs_row, null);
		}

		SyncGroup group = getItem(position);

		Collections.sort(group.syncs);
		
		List<View> views = new ArrayList<>(5);
		
		views.add(convertView.findViewById(R.id.txtWeekdaySun));
		views.add(convertView.findViewById(R.id.txtWeekdayMon));
		views.add(convertView.findViewById(R.id.txtWeekdayTue));
		views.add(convertView.findViewById(R.id.txtWeekdayWed));
		views.add(convertView.findViewById(R.id.txtWeekdayThu));
		views.add(convertView.findViewById(R.id.txtWeekdayFri));
		views.add(convertView.findViewById(R.id.txtWeekdaySat));
		
		for (View v : views) {
			v.setVisibility(View.GONE);
		}

		for (Sync sync : group.syncs) {
			Log.d("RideSyncer", "Making " + sync.getWeekday() + " Visible");
			views.get(sync.getWeekday() - 1).setVisibility(View.VISIBLE);
		}
		
		String username = "User" + group.syncs.get(0).getUserId();
		TextView txtRequestor = (TextView) convertView.findViewById(R.id.txt_requester);
		txtRequestor.setText(username);

		return convertView;
	}

}
