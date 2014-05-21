package com.gedutech.ridesyncer.models;

import java.util.ArrayList;
import java.util.List;

public class SyncGroup {
	
	public List<Sync> syncs;
	
	public SyncGroup() {
		this.syncs = new ArrayList<>();
	}
	
	public SyncGroup(List<Sync> syncs) {
		this.syncs = syncs;
	}

}
