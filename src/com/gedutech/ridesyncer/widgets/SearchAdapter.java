package com.gedutech.ridesyncer.widgets;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gedutech.ridesyncer.ProfilesActivity;
import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.User;

public class SearchAdapter extends ArrayAdapter<User> {

	public SearchAdapter(Context context, List<User> results) {
		super(context, 0, results);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
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

		final User user = getItem(position);

		vHolder.txtUsername.setText(user.getUsername());
		vHolder.txtLocation.setText(user.getCity() + ", " + user.getState());
		vHolder.txtDistance.setText(String.format("%.2f mi", user.getDistance()));
		
		vHolder.imgProfilePic.setTag(user.getId());
		vHolder.imgProfilePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ProfilesActivity.class);
				intent.putExtra("user_id", (long)v.getTag());
				getContext().startActivity(intent);	
			}
		});

		switch ((int) user.getId()) {
		case 6:
			vHolder.imgProfilePic.setImageResource(R.drawable.diego);
			break;
		case 7:
			vHolder.imgProfilePic.setImageResource(R.drawable.jesse);
			break;
		case 9:
			vHolder.imgProfilePic.setImageResource(R.drawable.sam);
			break;
		case 5:
			vHolder.imgProfilePic.setImageResource(R.drawable.tigran);
			break;
		default:
			vHolder.imgProfilePic.setImageResource(R.drawable.silhouette);
			break;
		}

		String weekdays = "";

		for (Schedule schedule : user.getSchedules()) {
			String weekday = "";
			switch (schedule.getWeekday()) {
			case 1:
				weekday = "Su";
				break;
			case 2:
				weekday = "M";
				break;
			case 3:
				weekday = "T";
				break;
			case 4:
				weekday = "W";
				break;
			case 5:
				weekday = "Th";
				break;
			case 6:
				weekday = "F";
				break;
			case 7:
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
		protected ImageView imgProfilePic;

		public ViewHolder(View v) {
			txtUsername = (TextView) v.findViewById(R.id.username);
			txtLocation = (TextView) v.findViewById(R.id.location);
			txtDistance = (TextView) v.findViewById(R.id.distance);
			txtWeekdays = (TextView) v.findViewById(R.id.weekdays);
			imgProfilePic = (ImageView) v.findViewById(R.id.profilePic);

			v.setTag(this);
		}
	}
}
