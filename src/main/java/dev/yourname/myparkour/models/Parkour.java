package dev.yourname.myparkour.models;

import dev.yourname.myparkour.controllers.ParkourManager;
import dev.yourname.myparkour.utils.ParkourDataUtils;
import dev.yourname.myparkour.controllers.ParkourPlayerManager;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Parkour {
	public File file;
	public String name;
	public Location spawnLocation;
	public List<PlayerParkourData> leaderboard = new ArrayList<>();

	/**
	 * Should be called when creating a new parkour
	 * @param name The name of the parkour
	 * @param spawnLocation The spawn location of the parkour, not necessarily the start block
	 */
	public Parkour(String name, Location spawnLocation) {
		this.name = name;
		this.spawnLocation = spawnLocation;

		ParkourDataUtils.createParkourDataFile(this); // TODO: step 2
		save(); // TODO: step 2

		ParkourManager.parkourList.add(this); // TODO: step 2
	}

	/**
	 * Should be called when loading an existing parkour
	 * @param file The file of the parkour
	 */
	public Parkour(File file) {
		this.file = file;
		load(); // TODO: step 2

		ParkourManager.parkourList.add(this); // TODO: step 2
	}

	// TODO: step 1
	public String getFormattedLocation() {
		return "(" + spawnLocation.getBlockX() + ", " + spawnLocation.getBlockY() + ", " + spawnLocation.getBlockZ() + ")";
	}

	// TODO: step 2
	public void load() {
		ParkourDataUtils.loadParkourData(this);
	}

	// TODO: step 2
	public void save() {
		ParkourDataUtils.saveParkourData(this);
	}

	// TODO: step 2
	public void delete() {
		ParkourPlayerManager.stopParkour(this);
		ParkourDataUtils.deleteParkourData(this);
	}
}
