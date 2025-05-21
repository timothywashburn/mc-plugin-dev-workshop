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

	public Parkour(String name, Location spawnLocation) {
		this.name = name;
		this.spawnLocation = spawnLocation;
	}

	public Parkour(File file) {
		this.file = file;
	}
}
