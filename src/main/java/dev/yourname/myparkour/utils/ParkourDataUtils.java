package dev.yourname.myparkour.utils;

import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.models.PlayerParkourData;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ParkourDataUtils {
	/**
	 * Loads all parkour data from the parkours directory.
	 */
	public static void loadParkourData() {
		File parkourDir = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours");
		parkourDir.mkdirs();
		for (File file : parkourDir.listFiles()) {
			if (file.getName().endsWith(".yml")) new Parkour(file);
		}
	}

	/**
	 * Creates a new parkour data file. Should be called when creating a new parkour,
	 * not when loading an existing one.
	 * @param parkour The parkour to create the data file for.
	 */
	public static void createParkourDataFile(Parkour parkour) {
		String fileName = parkour.name + ".yml";
		parkour.file = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours/" + fileName);
		if (parkour.file.exists()) throw new IllegalArgumentException("Parkour file already exists: " + parkour.file.getPath());
		parkour.file.getParentFile().mkdirs();
		try {
			parkour.file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Loads parkour data for an existing parkour.
	 * @param parkour The parkour to load data for.
	 */
	public static void loadParkourData(Parkour parkour) {
		File file = parkour.file;

		if (!file.exists()) {
			MyParkour.INSTANCE.getLogger().warning("Parkour file not found: " + file.getPath());
			return;
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		parkour.name = config.getString("name");
		if (parkour.name == null) {
			System.out.println("Parkour name not found in parkour file: " + file.getPath());
			return;
		}

		ConfigurationSection spawnLocation = config.getConfigurationSection("spawnLocation");
		if (spawnLocation != null) {
			double x = spawnLocation.getDouble("x");
			double y = spawnLocation.getDouble("y");
			double z = spawnLocation.getDouble("z");
			String worldName = spawnLocation.getString("world");
			if (worldName == null) {
				System.out.println("Parkour spawnLocation.world not found in parkour: " + parkour.name);
				return;
			}

			parkour.spawnLocation = new Location(MyParkour.INSTANCE.getServer().getWorld(worldName), x, y, z);
		} else {
			System.out.println("Parkour spawnLocation not found in parkour: " + parkour.name);
			return;
		}

		ConfigurationSection leaderboardSection = config.getConfigurationSection("leaderboard");
		if (leaderboardSection != null) {
			for (String key : leaderboardSection.getKeys(false)) {
				String playerName = leaderboardSection.getString(key + ".playerName");
				long time = leaderboardSection.getLong(key + ".time");
				int jumps = leaderboardSection.getInt(key + ".jumps");
				int deaths = leaderboardSection.getInt(key + ".deaths");

				PlayerParkourData entry = new PlayerParkourData(playerName, time, jumps, deaths);
				entry.isComplete = true;
				parkour.leaderboard.add(entry);
			}
		} else {
			System.out.println("Parkour leaderboard not found in parkour: " + parkour.name);
		}
	}

	/**
	 * Saves parkour data to the parkour file. Does not create a new file.
	 * @param parkour The parkour to save data for.
	 */
	public static void saveParkourData(Parkour parkour) {
		File file = parkour.file;
		if (!file.exists()) throw new RuntimeException("Parkour file does not exist: " + file.getPath());

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("name", parkour.name);

		config.set("spawnLocation.x", parkour.spawnLocation.getX());
		config.set("spawnLocation.y", parkour.spawnLocation.getY());
		config.set("spawnLocation.z", parkour.spawnLocation.getZ());
		config.set("spawnLocation.world", parkour.spawnLocation.getWorld().getName());

		config.set("leaderboard", null);
		for (int i = 0; i < parkour.leaderboard.size(); i++) {
			PlayerParkourData entry = parkour.leaderboard.get(i);
			if (!entry.isComplete) continue;
			config.set("leaderboard." + i + ".playerName", entry.playerName);
			config.set("leaderboard." + i + ".time", entry.ticks);
			config.set("leaderboard." + i + ".jumps", entry.jumps);
			config.set("leaderboard." + i + ".deaths", entry.deaths);
		}

		try {
			config.save(file);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Deletes the parkour data file. Should be called when deleting a parkour.
	 * @param parkour The parkour to delete data for.
	 */
	public static void deleteParkourData(Parkour parkour) {
		if (parkour.file.exists()) parkour.file.delete();
	}
}
