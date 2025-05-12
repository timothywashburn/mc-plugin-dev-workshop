package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.models.PlayerParkourData;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ParkourDataManager {
	public static void loadParkours() {
		File parkourDir = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours");
		parkourDir.mkdirs();
		for (File file : parkourDir.listFiles()) {
			if (file.getName().endsWith(".yml")) {
				Parkour parkour = new Parkour(file);
				ParkourManager.parkourList.add(parkour);
			}
		}
	}

	public static void createParkourFile(Parkour parkour) {
		String fileName = parkour.name.replace(' ', '-') + ".yml";
		parkour.file = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours/" + fileName);
		if (parkour.file.exists()) throw new IllegalArgumentException("Parkour file already exists: " + parkour.file.getPath());
		parkour.file.mkdirs();
	}

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

		ConfigurationSection location = config.getConfigurationSection("spawnLocation");
		if (location != null) {
			double x = location.getDouble("x");
			double y = location.getDouble("y");
			double z = location.getDouble("z");
			String worldName = location.getString("world");
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
				parkour.leaderboard.add(entry);
			}
		} else {
			System.out.println("Parkour leaderboard not found in parkour: " + parkour.name);
		}
	}

	public static void save(Parkour parkour) {
		File file = parkour.file;
		file.mkdirs();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("name", parkour.name);

		config.set("spawnLocation.x", parkour.spawnLocation.getX());
		config.set("spawnLocation.y", parkour.spawnLocation.getY());
		config.set("spawnLocation.z", parkour.spawnLocation.getZ());
		config.set("spawnLocation.world", parkour.spawnLocation.getWorld().getName());

		for (int i = 0; i < parkour.leaderboard.size(); i++) {
			PlayerParkourData entry = parkour.leaderboard.get(i);
			config.set("leaderboard." + i + ".playerName", entry.playerName);
			config.set("leaderboard." + i + ".time", entry.ticks);
			config.set("leaderboard." + i + ".jumps", entry.jumps);
			config.set("leaderboard." + i + ".deaths", entry.deaths);
		}

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delete(Parkour parkour) {
		if (parkour.file.exists()) parkour.file.delete();
	}
}
