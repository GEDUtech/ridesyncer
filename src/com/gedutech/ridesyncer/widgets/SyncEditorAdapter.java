package com.gedutech.ridesyncer.widgets;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.utils.TimeUtil;

public class SyncEditorAdapter extends BaseExpandableListAdapter {

	protected Date weekStart;

	protected Date weekEnd;

	protected Context context;

	protected List<Sync> syncs;

	protected User authUser;

	protected Map<Sync, List<SyncUser>> filtered;

	public SyncEditorAdapter(Context context, List<Sync> syncs, User authUser) {
		super();

		this.context = context;
		this.syncs = syncs;
		this.authUser = authUser;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		final SyncUser syncUser = getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.syncs_expanaded_list_row, null);
		}

		return convertView;
	}

	@Override
	public SyncUser getChild(int groupPosition, int childPosition) {
		return filtered.get(getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return getChild(groupPosition, childPosition).getId();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return filtered.get(getGroup(groupPosition)).size();
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
			convertView = inflater.inflate(R.layout.sync_editor_row, parent, false);
			vHolder = new ViewHolder(convertView);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		Sync sync = getGroup(groupPosition);
		vHolder.txtWeekday.setText(TimeUtil.shortWeekday(sync.getWeekday()));

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

		public ViewHolder(View v) {
			txtWeekday = (TextView) v.findViewById(R.id.weekday);

			v.setTag(this);
		}
	}
}
