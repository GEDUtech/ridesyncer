package com.gedutech.ridesyncer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.UsersApi;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.SearchAdapter;

public class SearchFragment extends Fragment {

	private User authUser;
	private Session session;
	private List<User> results;
	private SearchAdapter adapter;
	private UsersApi usersApi;
	private UserSearchTask mUserSearchTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = Session.getInstance(getActivity().getApplicationContext());
		authUser = session.getAuthUser();
		usersApi = new UsersApi(authUser.getToken());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		results = new ArrayList<>();

		ListView listView = (ListView) getView().findViewById(R.id.lstSearch);
		adapter = new SearchAdapter(getActivity(), results);
		listView.setAdapter(adapter);

		mUserSearchTask = new UserSearchTask();
		mUserSearchTask.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		return view;
	}

	protected class UserSearchTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... params) {
			ApiResult result = null;
			// try {
			result = usersApi.search();
			// } catch (JSONException e) {
			// progressSwitcher.showProgress(false);
			// cancel(true);
			// }
			return result;
		}

		@Override
		protected void onPostExecute(ApiResult result) {
			super.onPostExecute(result);

			mUserSearchTask = null;
			// progressSwitcher.showProgress(false);

			if (result.isSuccess()) {
				Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

				try {
					Log.d("RideSyncer", result.getData().toString(4));

					JSONArray resultsArr = result.getData().getJSONArray("results");

					for (int i = 0; i < resultsArr.length(); i++) {
						results.add(User.fromJSON(resultsArr.getJSONObject(i)));
					}
				} catch (Exception e) {
					Log.d("RideSyncer", "onPostExecute: " + e.getMessage());
				}
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
				Log.d("RideSyncer", result.getRaw());
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			mUserSearchTask = null;
			// progressSwitcher.showProgress(false);
		}

	}

}
