package com.gedutech.ridesyncer.widgets;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.SyncManager;
import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;

public class SyncEditorAdapter extends ArrayAdapter<User> implements OnClickListener, OnCheckedChangeListener,
		OnItemSelectedListener {

	protected SyncManager syncManager;

	public SyncEditorAdapter(Context context, SyncManager syncManager, List<User> objects) {
		super(context, 0, objects);
		this.syncManager = syncManager;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
		params.gravity = Gravity.CENTER;

		List<Integer> weekdays = syncManager.getWeekdays();

		ViewGroup row = new LinearLayout(getContext());
		row.setPadding(10, 10, 10, 10);

		User user = getItem(position);

		TextView txtUsername = new TextView(getContext());
		txtUsername.setText(user.getUsername());
		txtUsername.setLayoutParams(params);
		txtUsername.setGravity(Gravity.CENTER);
		txtUsername.setTextColor(getContext().getResources().getColor(R.color.dark));
		txtUsername.setTypeface(null, Typeface.BOLD);
		row.addView(txtUsername);

		for (int weekday : weekdays) {
			Schedule schedule = user.getScheduleOnWeekday(weekday);
			View view;
			if (schedule == null) {
				view = new View(getContext());
			} else {
				SyncUser syncUser = syncManager.getSyncUserForSchedule(schedule);

				LinearLayout relLayout = new LinearLayout(getContext());
				relLayout.setPadding(0, 0, 0, 0);
				relLayout.setGravity(Gravity.CENTER);

				CheckBox checkbox = new CheckBox(getContext());
				checkbox.setOnCheckedChangeListener(this);
				checkbox.setTag(syncUser);
				checkbox.setTag(R.string.weekday, weekday);
				checkbox.setChecked(syncUser.getStatus() == 1);

				int numDrivers = syncManager.numDriversOnWeekday(weekday);
				if (numDrivers > 1 && syncUser.getStatus() == 1) {
					Spinner spinner = createSpinner(weekday, syncUser);

					Button btn = new Button(getContext());
					btn.setBackgroundResource(android.R.drawable.ic_menu_revert);
					btn.setTag(syncUser);
					btn.setTag(R.string.weekday, weekday);
					btn.setOnClickListener(this);

					relLayout.addView(spinner);
					relLayout.addView(btn);
				} else {
					relLayout.addView(checkbox);
				}
				view = relLayout;
			}
			view.setLayoutParams(params);
			row.addView(view);
		}

		return row;
	}

	protected Spinner createSpinner(int weekday, SyncUser syncUser) {
		String[] items = syncManager.getDriverListSpinnerData(weekday);
		Spinner spinner = new Spinner(getContext());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_dropdown_item, items);
		spinner.setAdapter(adapter);
		spinner.setPadding(0, 0, 0, 0);
		spinner.setTag(syncUser);
		spinner.setTag(R.string.weekday, weekday);
		spinner.setOnItemSelectedListener(this);

		spinner.setSelection(syncUser.getOrder() - 1, false);

		return spinner;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SyncUser syncUser = (SyncUser) buttonView.getTag();
		syncUser.setStatus(isChecked ? 1 : 2);

		int weekday = (int) buttonView.getTag(R.string.weekday);
		int numDrivers = syncManager.numDriversOnWeekday(weekday);

		if (syncUser.getOrder() == 0) {
			syncUser.setOrder(numDrivers);
		}

		notifyDataSetChanged();
	}

	public void onClick(View v) {
		SyncUser syncUser = (SyncUser) v.getTag();
		int weekday = (int) v.getTag(R.string.weekday);
		syncUser.setStatus(2);
		syncManager.revertOrder(weekday, syncUser);
		notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		SyncUser syncUser = (SyncUser) parentView.getTag();
		int weekday = (int) parentView.getTag(R.string.weekday);

		syncManager.swapOrder(weekday, position + 1, syncUser);
		notifyDataSetChanged();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parentView) {

	}

}
