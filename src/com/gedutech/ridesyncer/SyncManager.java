package com.gedutech.ridesyncer;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gedutech.ridesyncer.models.Schedule;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;

public class SyncManager {

	protected User user;
	protected List<User> others;
	protected List<Integer> weekdays;

	protected Map<Integer, Sync> syncs;

	public SyncManager(User user, List<User> others) {
		this.user = user;
		this.others = others;

		syncs = new HashMap<>();
		weekdays = new ArrayList<>();

		for (Schedule schedule : user.getSchedules()) {
			int weekday = schedule.getWeekday();

			Sync sync = new Sync();
			sync.setWeekday(weekday);
			sync.getSyncUsers().add(newSyncUser(user));

			boolean addedWeekday = false;
			for (User other : others) {
				if (other.getScheduleOnWeekday(weekday) != null) {
					sync.getSyncUsers().add(newSyncUser(other));

					if (!addedWeekday) {
						weekdays.add(weekday);
						addedWeekday = true;
					}
				}
			}
			if (addedWeekday) {
				syncs.put(weekday, sync);
			}
		}
	}

	public int numDriversOnWeekday(int weekday) {
		int count = 0;
		for (SyncUser syncUser : syncs.get(weekday).getSyncUsers()) {
			if (syncUser.getOrder() > 0) {
				count++;
			}
		}
		return count;
	}

	public void validate() throws Exception {
		for (int weekday : syncs.keySet()) {
			int numDrivers = numDriversOnWeekday(weekday);
			if (numDrivers == 0) {
				throw new Exception(String.format("There are no drivers on %s", weekdayLabel(weekday, false)));
			} else if (numDrivers > 1) {
				// hrmm?
			}
		}
	}

	public String[] getDriverListSpinnerData(int weekday) {
		int numDrivers = numDriversOnWeekday(weekday);
		String[] items = new String[numDrivers];
		for (int i = 0; i < numDrivers; i++) {
			items[i] = (i + 1) + "";
		}
		return items;
	}

	public void revertOrder(int weekday, SyncUser syncUser) {
		for (SyncUser su : syncs.get(weekday).getSyncUsers()) {
			if (su.getOrder() > syncUser.getOrder()) {
				su.setOrder(su.getOrder() - 1);
			}
		}
		syncUser.setOrder(0);
	}

	public void swapOrder(int weekday, int order, SyncUser syncUser) {
		int oldOrder = syncUser.getOrder();
		for (SyncUser su : syncs.get(weekday).getSyncUsers()) {
			if (su.getOrder() == order) {
				su.setOrder(oldOrder);
				break;
			}
		}
		syncUser.setOrder(order);
	}

	public SyncUser getSyncUserForSchedule(Schedule schedule) {
		Sync sync = syncs.get(schedule.getWeekday());
		for (SyncUser syncUser : sync.getSyncUsers()) {
			if (syncUser.getUserId() == schedule.getUserId()) {
				return syncUser;
			}
		}
		return null;
	}

	public List<Integer> getWeekdays() {
		return weekdays;
	}

	public Map<Integer, Sync> getSync() {
		return syncs;
	}

	public String weekdayLabel(int weekday, boolean shortFormat) {
		DateFormatSymbols symbols = new DateFormatSymbols();
		return shortFormat ? symbols.getShortWeekdays()[weekday + 1] : symbols.getWeekdays()[weekday + 1];
	}
	
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>(others.size() + 1);
		users.add(user);
		users.addAll(others);
		return users;
	}

	public List<String> getHeaders() {
		List<String> headers = new ArrayList<>();
		headers.add("");
		// TODO: need to sort by weekday?
		for (int weekday : weekdays) {
			headers.add(weekdayLabel(weekday, true));
		}

		return headers;
	}

	protected SyncUser newSyncUser(User user) {
		SyncUser syncUser = new SyncUser();
		syncUser.setuserId(user.getId());
		return syncUser;
	}
}
