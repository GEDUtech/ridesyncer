package com.gedutech.ridesyncer.widgets;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.User;

public class SearchAdapter extends ArrayAdapter<User> {

	public SearchAdapter(Context context, List<User> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_list_view_row, null);
			vHolder = new ViewHolder(convertView);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		User user = getItem(position);

		vHolder.txtUsername.setText(user.getUsername());
		vHolder.txtLocation.setText(user.getCity() + ", " + user.getState());
		vHolder.txtDistance.setText(String.format("%.2f", user.getDistance()));

		String weekdays = "";

		for (Schedule schedule : user.getSchedules()) {
			String weekday = "";
			switch (schedule.getWeekday()) {
			case 0:
				weekday = "Su";
				break;
			case 1:
				weekday = "M";
				break;
			case 2:
				weekday = "T";
				break;
			case 3:
				weekday = "W";
				break;
			case 4:
				weekday = "Th";
				break;
			case 5:
				weekday = "F";
				break;
			case 6:
				weekday = "S";
				break;
			}

			weekdays += weekday + " ";
		}

		vHolder.txtWeekdays.setText(weekdays.trim());

		return convertView;
	}

	static class ViewHolder {

		protected TextView txtUsername;
		protected TextView txtDistance;
		protected TextView txtLocation;
		protected TextView txtWeekdays;

		public ViewHolder(View v) {
			txtUsername = (TextView) v.findViewById(R.id.username);
			txtLocation = (TextView) v.findViewById(R.id.location);
			txtDistance = (TextView) v.findViewById(R.id.distance);
			txtWeekdays = (TextView) v.findViewById(R.id.weekdays);

			v.setTag(this);
		}
	}
}