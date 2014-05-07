package com.gedutech.ridesyncer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.SchedulesAdapter;

public class SchedulesFragment extends Fragment {

	private SchedulesAdapter adapter;
	private User authUser;
	private Session session;
	private View header;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = Session.getInstance(getActivity());
		authUser = session.getAuthUser();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new SchedulesAdapter(getActivity(), authUser.getSchedules());

		ListView listView = (ListView) getView().findViewById(R.id.lstSchedules);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		
		getView().findViewById(R.id.btnAddSchedule).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedules, container, false);
		header = inflater.inflate(R.layout.schedules_list_view_header, null, false);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

}
