package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParkourManager implements Listener {
	public static final List<Parkour> parkours = new ArrayList<>();
	private static final List<Player> parkourPlayers = new ArrayList<>();

	public ParkourManager() {
		loadParkours();
	}

	public static void loadParkours() {
		File parkourDir = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours");
		parkourDir.mkdirs();
		for (File file : parkourDir.listFiles()) {
			if (file.getName().endsWith(".yml")) {
				Parkour parkour = new Parkour(file);
				parkours.add(parkour);
			}
		}
	}

	public static Parkour getParkour(String name) {
		for (Parkour parkour : parkours) if (parkour.name.equalsIgnoreCase(name)) return parkour;
		return null;
	}

	public static void createParkour(String name, Location location) {
		Parkour parkour = new Parkour(name, location);
		parkour.spawnLocation = location;

		parkours.add(parkour);
	}

	public static void deleteParkour(String name) {
		Parkour parkourToDelete = null;
		for (Parkour parkour : parkours) {
			if (parkour.name.equalsIgnoreCase(name)) {
				parkourToDelete = parkour;
				break;
			}
		}
		if (parkourToDelete != null) {
			parkours.remove(parkourToDelete);
		}
	}
}
