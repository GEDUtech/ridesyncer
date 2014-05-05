package com.gedutech.ridesyncer.widgets;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.utils.TimeUtil;

public class SyncsAdapter extends ArrayAdapter<Sync> {

	protected Date weekStart;

	protected Date weekEnd;

	public Date getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(Date weekStart) {
		this.weekStart = weekStart;
	}

	public Date getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(Date weekEnd) {
		this.weekEnd = weekEnd;
	}

	public SyncsAdapter(Context context, List<Sync> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.syncs_list_view_row, parent, false);

			vHolder = new ViewHolder(convertView);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		Sync sync = getItem(position);
		SyncUser syncUser = getDriverForToday(sync);

		vHolder.txtWeekday.setText(TimeUtil.shortWeekday(sync.getWeekday()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(weekStart);
		cal.set(Calendar.DAY_OF_WEEK, sync.getWeekday() + 1);

		vHolder.txtDate.setText(TimeUtil.format(cal.getTime(), "MM/dd/yyyy"));

		if (syncUser == null) {
			vHolder.txtUsername.setText("-");
			vHolder.txtName.setText("Sync not started yet");
			vHolder.txtTime.setText("-");
		} else {
			vHolder.txtUsername.setText(syncUser.getUser().getUsername());
			vHolder.txtName.setText(syncUser.getUser().getFirstName() + " " + syncUser.getUser().getLastName());

			Date earliest = sync.earlistSchedule();
			Date latest = sync.latestSchedule();
			vHolder.txtTime.setText(TimeUtil.formatTime12(earliest) + " - " + TimeUtil.formatTime12(latest));
		}

		return convertView;
	}

	protected SyncUser getDriverForToday(Sync sync) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(weekStart);
		cal.set(Calendar.DAY_OF_WEEK, sync.getWeekday() + 1);

		if (cal.getTime().before(sync.getCreatedAt())) {
			return null;
		}

		List<SyncUser> drivers = sync.getDrivers();
		if (drivers.size() == 1) {
			return drivers.get(0);
		}

		for (SyncUser syncUser : drivers) {
			cal.setTime(sync.getCreatedAt());
			cal.add(Calendar.WEEK_OF_YEAR, syncUser.getOrder() - 1);
			int weeksDiff = weeksDiff(sync.getCreatedAt(), weekStart);

			if (weeksDiff < drivers.size()) {
				if (syncUser.getOrder() == weeksDiff + 1) {
					return syncUser;
				}
			} else if ((weeksDiff - syncUser.getOrder() + 1) % drivers.size() == 0) {
				return syncUser;
			}
		}

		return null;
	}

	protected int weeksDiff(Date d1, Date d2) {
		return Weeks.weeksBetween(new DateTime(d1), new DateTime(d2)).getWeeks();
	}

	static class ViewHolder {

		TextView txtWeekday;
		TextView txtUsername;
		TextView txtName;
		TextView txtDate;
		TextView txtTime;

		public ViewHolder(View v) {
			txtWeekday = (TextView) v.findViewById(R.id.weekday);
			txtUsername = (TextView) v.findViewById(R.id.username);
			txtName = (TextView) v.findViewById(R.id.name);
			txtDate = (TextView) v.findViewById(R.id.date);
			txtTime = (TextView) v.findViewById(R.id.time);

			v.setTag(this);
		}
	}
}
