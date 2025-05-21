package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.utils.ParkourDataUtils;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

// TODO: step 1
public class ParkourManager {
	public static final List<Parkour> parkourList = new ArrayList<>();

	public static void init() {
		ParkourDataUtils.loadParkourData();
	}

	public static Parkour getParkour(String name) {
		for (Parkour parkour : parkourList) if (parkour.name.equalsIgnoreCase(name)) return parkour;
		return null;
	}

	public static void createParkour(String name, Location spawnLocation) {
		Parkour parkour = new Parkour(name, spawnLocation);
		parkour.spawnLocation = spawnLocation;
	}

	public static void deleteParkour(String name) {
		Parkour parkour = null;
		for (Parkour testParkour : parkourList) {
			if (!testParkour.name.equalsIgnoreCase(name)) continue;
			parkour = testParkour;
			break;
		}
		if (parkour == null) return;

		parkourList.remove(parkour);
		parkour.delete();
	}
}
