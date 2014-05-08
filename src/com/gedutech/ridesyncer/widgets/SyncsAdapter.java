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
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.Session;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.utils.TimeUtil;

public class SyncsAdapter extends BaseExpandableListAdapter {

	protected Date weekStart;

	protected Date weekEnd;

	protected Context context;

	protected List<Sync> syncs;

	public SyncsAdapter(Context context, List<Sync> syncs) {
		super();

		this.context = context;
		this.syncs = syncs;
	}

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

	@Override
	public SyncUser getChild(int groupPosition, int childPosition) {
		return syncs.get(groupPosition).getSyncUsers().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return getChild(groupPosition, childPosition).getId();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		final SyncUser syncUser = getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.syncs_expanaded_list_row, null);
		}

		int pl = convertView.getPaddingLeft();
		int pt = convertView.getPaddingTop();
		int pr = convertView.getPaddingRight();
		int pb = convertView.getPaddingBottom();
		if (childPosition == 0) {
			convertView.setBackgroundResource(R.drawable.child_list_row);
		} else {
			convertView.setBackgroundResource(R.color.white);
		}
		convertView.setPadding(pl, pt, pr, pb);

		TextView txtUsername = (TextView) convertView.findViewById(R.id.username);
		TextView txtName = (TextView) convertView.findViewById(R.id.name);

		txtUsername.setText(syncUser.getUser().getUsername());
		txtName.setText(syncUser.getUser().getFirstName() + " " + syncUser.getUser().getLastName());

		if (Session.getInstance(context).getAuthUser().getId() == syncUser.getUserId()) {
			convertView.findViewById(R.id.actions).setVisibility(View.GONE);
		} else {
			convertView.findViewById(R.id.actions).setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return getGroup(groupPosition).getSyncUsers().size();
	}

	@Override
	public Sync getGroup(int groupPosition) {
		return syncs.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return syncs.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return getGroup(groupPosition).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder vHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.syncs_list_view_row, parent, false);

			vHolder = new ViewHolder(convertView);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		Sync sync = getGroup(groupPosition);
		SyncUser syncUser = getDriverForToday(sync);

		vHolder.txtWeekday.setText(TimeUtil.shortWeekday(sync.getWeekday()));
		Calendar cal = Calendar.getInstance();
		cal.setTime(weekStart);
		cal.set(Calendar.DAY_OF_WEEK, sync.getWeekday() + 1);

		vHolder.txtDate.setText(TimeUtil.format(cal.getTime(), "MM/dd/yyyy"));

		if (syncUser == null) {
			vHolder.txtUsername.setText("-");
			vHolder.txtName.setText("Sync not started yet");
			vHolder.txtStartTime.setText("");
			vHolder.txtEndTime.setText("");
		} else {
			vHolder.txtUsername.setText(syncUser.getUser().getUsername());
			vHolder.txtName.setText(syncUser.getUser().getFirstName() + " " + syncUser.getUser().getLastName());

			Date earliest = sync.earlistSchedule();
			Date latest = sync.latestSchedule();
			vHolder.txtStartTime.setText(TimeUtil.formatTime12(earliest));
			vHolder.txtEndTime.setText(TimeUtil.formatTime12(latest));
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

	static class ViewHolder {

		TextView txtWeekday;
		TextView txtUsername;
		TextView txtName;
		TextView txtDate;
		TextView txtStartTime;
		TextView txtEndTime;

		public ViewHolder(View v) {
			txtWeekday = (TextView) v.findViewById(R.id.weekday);
			txtUsername = (TextView) v.findViewById(R.id.username);
			txtName = (TextView) v.findViewById(R.id.name);
			txtDate = (TextView) v.findViewById(R.id.date);
			txtStartTime = (TextView) v.findViewById(R.id.start_time);
			txtEndTime = (TextView) v.findViewById(R.id.end_time);

			v.setTag(this);
		}
	}
}
