package com.gedutech.ridesyncer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

		Log.d("RideSyncer", authUser.getSchedules().size() + " ");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("RideSyncer", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

		adapter = new SchedulesAdapter(getActivity(), authUser.getSchedules());

		ListView listView = (ListView) getView().findViewById(R.id.lstSchedules);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedules, container, false);
		header = inflater.inflate(R.layout.schedules_list_view_header, null, false);
		return view;
	}

}
