package com.gedutech.ridesyncer;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.SchedulesAdapter;

public class SchedulesFragment extends Fragment {

	private SchedulesAdapter adapter;
	private User authUser;

	public SchedulesFragment(User authUser) {
		this.authUser = authUser;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO: Temporary, delete
		// Begin remove
		Schedule s1 = new Schedule();
		s1.setWeekday(0);
		s1.setStart(new Date());
		s1.setEnd(new Date());

		Schedule s2 = new Schedule();
		s2.setWeekday(1);
		s2.setStart(new Date());
		s2.setEnd(new Date());

		Schedule s3 = new Schedule();
		s3.setWeekday(2);
		s3.setStart(new Date());
		s3.setEnd(new Date());

		authUser.getSchedules().add(s1);
		authUser.getSchedules().add(s2);
		authUser.getSchedules().add(s3);
		// End remove
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new SchedulesAdapter(getActivity(), authUser.getSchedules());

		ListView listView = (ListView) getView().findViewById(R.id.lstSchedules);
		listView.setAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_schedules, container, false);
	}

}
