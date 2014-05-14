package com.gedutech.ridesyncer.widgets;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.models.Sync;

public class PendingSyncsAdapter extends ArrayAdapter<Sync> {

	public PendingSyncsAdapter(Context context, List<Sync> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalater.inflate(R.layout.pending_syncs_row, null);
		}

		return convertView;
	}

}
