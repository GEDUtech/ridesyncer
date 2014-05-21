package com.gedutech.ridesyncer.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.Session;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncGroup;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.utils.TimeUtil;

public class ReviewSyncGroupAdapter extends BaseExpandableListAdapter {

	protected Context context;

	protected SyncGroup group;

	public ReviewSyncGroupAdapter(Context context, SyncGroup group) {
		super();

		this.context = context;
		this.group = group;
	}

	@Override
	public SyncUser getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition).getSyncUsers().get(childPosition);
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
		ImageView imgProfilePic = (ImageView) convertView.findViewById(R.id.profilePic);
		ImageView imgPosition = (ImageView) convertView.findViewById(R.id.position);
		imgPosition.setVisibility(View.VISIBLE);
		if (syncUser.getOrder() == 0) {
			imgPosition.setImageResource(R.drawable.passenger);
		} else {
			imgPosition.setImageResource(R.drawable.driver);
		}

		txtUsername.setText(syncUser.getUser().getUsername());
		txtName.setText(syncUser.getUser().getFirstName() + " " + syncUser.getUser().getLastName());

		switch ((int) syncUser.getUserId()) {
		case 6:
			imgProfilePic.setImageResource(R.drawable.diego);
			break;
		case 7:
			imgProfilePic.setImageResource(R.drawable.jesse);
			break;
		case 9:
			imgProfilePic.setImageResource(R.drawable.sam);
			break;
		case 5:
			imgProfilePic.setImageResource(R.drawable.tigran);
			break;
		default:
			imgProfilePic.setImageResource(R.drawable.silhouette);
			break;
		}

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
		return group.syncs.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return group.syncs.size();
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
			convertView = inflater.inflate(R.layout.sync_group_list_row, parent, false);

			vHolder = new ViewHolder(convertView);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		Sync sync = getGroup(groupPosition);
		vHolder.txtWeekday.setText(TimeUtil.weekday(sync.getWeekday(), "EEEEE"));

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
			txtWeekday = (TextView) v.findViewById(R.id.txt_weekday);
			v.setTag(this);
		}
	}
}
