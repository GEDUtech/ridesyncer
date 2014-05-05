package com.gedutech.ridesyncer;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.utils.TimeUtil;
import com.gedutech.ridesyncer.widgets.SyncsAdapter;

public class SyncsFragment extends Fragment {

	protected Session session;
	protected User authUser;
	protected SyncsAdapter adapter;
	protected int week;

	protected TextView txtWeekRange;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = Session.getInstance(getActivity().getApplicationContext());
		authUser = session.getAuthUser();
		Collections.sort(authUser.getSyncs());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ExpandableListView listView = (ExpandableListView) getView().findViewById(R.id.lstSyncs);
		adapter = new SyncsAdapter(getActivity(), authUser.getSyncs());
		changeWeek();
		listView.setAdapter(adapter);

		getView().findViewById(R.id.btnNextWeek).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				week++;
				changeWeek();
			}
		});

		getView().findViewById(R.id.btnPrevWeek).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				week--;
				changeWeek();
			}
		});
	}

	protected void changeWeek() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		Date weekStart = cal.getTime();

		cal.add(Calendar.DATE, 6);
		Date weekEnd = cal.getTime();

		String weekRange = TimeUtil.format(weekStart, "MM/dd/yy") + " - " + TimeUtil.format(weekEnd, "MM/dd/yy");
		txtWeekRange.setText(weekRange);

		adapter.setWeekStart(weekStart);
		adapter.setWeekEnd(weekEnd);
		adapter.notifyDataSetInvalidated();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_syncs, container, false);
		txtWeekRange = (TextView) view.findViewById(R.id.txtWeekRange);

		return view;
	}
}
