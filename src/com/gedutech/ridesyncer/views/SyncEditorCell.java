package com.gedutech.ridesyncer.views;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gedutech.ridesyncer.R;
import com.gedutech.ridesyncer.SyncManager;
import com.gedutech.ridesyncer.models.SyncUser;

public class SyncEditorCell extends LinearLayout {

	protected SyncUser syncUser;

	protected int weekday;

	protected SyncManager syncManager;

	protected Spinner spinner;

	protected CheckBox checkbox;

	ArrayAdapter<String> adapter;

	protected boolean inhibit;

	protected CellStateChangedListener cellStateChangedListener;

	protected WeekdayCheckedListener weekdayCheckedListener;

	public SyncEditorCell(Context context, final SyncManager syncManager, final int weekday, final SyncUser syncUser) {
		super(context);
		inflate(context, R.layout.sync_editor_cell, this);

		setSyncUser(syncUser);
		setWeekday(weekday);
		this.syncManager = syncManager;

		spinner = (Spinner) findViewById(R.id.spinnerOrder);
		adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				syncManager.swapOrder(weekday, position + 1, syncUser);
				if (cellStateChangedListener != null && !inhibit) {
					Log.d("RideSyncer", "on item selected");
					cellStateChangedListener.onStateChanged(SyncEditorCell.this);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

		checkbox = (CheckBox) findViewById(android.R.id.checkbox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int numDrivers = syncManager.numDriversOnWeekday(weekday);

				if (!isChecked) {
					syncUser.setOrder(0);
				} else if (isChecked && syncUser.getOrder() == 0) {
					syncUser.setOrder(numDrivers + 1);
				}

				if (cellStateChangedListener != null) {
					cellStateChangedListener.onStateChanged(SyncEditorCell.this);
				}
			}
		});

		((ImageButton) findViewById(R.id.btn_revert)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("RideSyncer", "Reverting...");
				syncManager.revertOrder(weekday, syncUser);
				cellStateChangedListener.onStateChanged(SyncEditorCell.this);
			}
		});

		update();
	}

	public void setOnWeekdayCheckedListener(WeekdayCheckedListener listener) {
		weekdayCheckedListener = listener;
	}

	public void setOnCellStateChangedListener(CellStateChangedListener listener) {
		cellStateChangedListener = listener;
	}

	public void update() {
		int numDrivers = syncManager.numDriversOnWeekday(weekday);

		if (numDrivers > 1 && syncUser.getOrder() > 0) {
			findViewById(R.id.sync_cell_multiple).setVisibility(View.VISIBLE);
			checkbox.setVisibility(View.GONE);

			updateSpinnerAdapter();
			inhibit = true;
			// Y U NO WORK!?!
			spinner.setSelection(syncUser.getOrder() - 1, false);
			inhibit = false;
		} else {
			findViewById(R.id.sync_cell_multiple).setVisibility(View.GONE);
			checkbox.setVisibility(View.VISIBLE);

			checkbox.setChecked(syncUser.getOrder() > 0);
		}
	}

	protected void updateSpinnerAdapter() {
		String[] items = syncManager.getDriverListSpinnerData(weekday);
		adapter.clear();
		adapter.addAll(items);
		adapter.notifyDataSetChanged();
	}

	public SyncUser getSyncUser() {
		return syncUser;
	}

	public void setSyncUser(SyncUser syncUser) {
		this.syncUser = syncUser;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public interface CellStateChangedListener {
		public void onStateChanged(SyncEditorCell cell);
	}

	public interface WeekdayCheckedListener {
		public void onWeekdayChecked(View v, boolean checked);
	}

}
