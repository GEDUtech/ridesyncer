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
import com.gedutech.ridesyncer.utils.TimeUtil;

public class SchedulesAdapter extends ArrayAdapter<Schedule> {

	public SchedulesAdapter(Context context, List<Schedule> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.schedules_list_view_row, null);
		}

		TextView weekdayView = (TextView) convertView.findViewById(R.id.txtWeekday);
		TextView startView = (TextView) convertView.findViewById(R.id.txtStart);
		TextView endView = (TextView) convertView.findViewById(R.id.txtEnd);

		Schedule schedule = getItem(position);

		weekdayView.setText(TimeUtil.shortWeekday(schedule.getWeekday()));
		startView.setText(TimeUtil.shortTime(schedule.getStart()));
		endView.setText(TimeUtil.shortTime(schedule.getEnd()));

		return convertView;
	}

}
