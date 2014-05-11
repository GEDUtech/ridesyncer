package com.gedutech.ridesyncer;

import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.UsersApi;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.widgets.SearchAdapter;

public class SearchFragment extends Fragment {

	private User authUser;
	private Session session;
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

		final ListView listView = (ListView) getView().findViewById(R.id.lstSearch);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (listView.getCheckedItemCount() > 2) {
					listView.setItemChecked(position, false);
				}
			}
		});
		adapter = new SearchAdapter(getActivity(), session.getMatches());
		listView.setAdapter(adapter);

		getView().findViewById(R.id.btnSyncEditor).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long[] ids = listView.getCheckedItemIds();

				if (ids.length == 0) {
					Toast.makeText(getActivity(), "No matches selected", Toast.LENGTH_SHORT).show();
					return;
				}

				Intent intent = new Intent(getActivity(), SyncEditorActivity.class);
				intent.putExtra("ids", ids);
				startActivity(intent);
			}
		});

		if (session.getMatches().size() == 0) {
			mUserSearchTask = new UserSearchTask();
			mUserSearchTask.execute();
		} else {
			adapter.notifyDataSetChanged();
		}
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
				// Toast.makeText(getActivity(), "Success",
				// Toast.LENGTH_LONG).show();

				try {
					// Log.d("RideSyncer", result.getData().toString(4));

					List<User> matches = session.getMatches();
					JSONArray resultsArr = result.getData().getJSONArray("results");
					for (int i = 0; i < resultsArr.length(); i++) {
						matches.add(User.fromJSON(resultsArr.getJSONObject(i)));
					}
				} catch (Exception e) {
					Log.d("RideSyncer", "onPostExecute: " + e.getMessage());
				}
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
				// Log.d("RideSyncer", result.getRaw());
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
