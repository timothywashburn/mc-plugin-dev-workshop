package dev.yourname.myparkour.models;

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
	}

	/**
	 * Should be called when loading an existing parkour
	 * @param file The file of the parkour
	 */
	public Parkour(File file) {
		this.file = file;
	}
}
