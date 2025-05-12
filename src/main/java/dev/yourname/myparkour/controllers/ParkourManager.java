package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.models.Parkour;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParkourManager {
	public static final List<Parkour> parkourList = new ArrayList<>();

	public ParkourManager() {
		ParkourDataManager.loadParkours();
	}

	public static Parkour getParkour(String name) {
		for (Parkour parkour : parkourList) if (parkour.name.equalsIgnoreCase(name)) return parkour;
		return null;
	}

	public static void createParkour(String name, Location location) {
		Parkour parkour = new Parkour(name, location);
		parkour.spawnLocation = location;

		parkourList.add(parkour);
	}

	public static void deleteParkour(String name) {
		Parkour parkourToDelete = null;
		for (Parkour parkour : parkourList) {
			if (parkour.name.equalsIgnoreCase(name)) {
				parkourToDelete = parkour;
				break;
			}
		}
		if (parkourToDelete != null) {
			parkourList.remove(parkourToDelete);
		}
	}
}
