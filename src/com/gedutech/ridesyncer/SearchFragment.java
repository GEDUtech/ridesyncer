package com.gedutech.ridesyncer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gedutech.ridesyncer.models.User;

public class SearchFragment extends Fragment {

	private User authUser;
	private Session session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = Session.getInstance(getActivity().getApplicationContext());
		authUser = session.getAuthUser();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ListView listView = (ListView) getView().findViewById(R.id.lstSearch);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		return view;
	}

}
