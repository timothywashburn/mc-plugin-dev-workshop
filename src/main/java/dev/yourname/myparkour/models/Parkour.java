package dev.yourname.myparkour.models;

import dev.yourname.myparkour.MyParkour;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Parkour {
	private File file;

	public String name;
	public Location spawnLocation;
	public List<LeaderboardEntry> leaderboard = new ArrayList<>();

	public Parkour(String name, Location spawnLocation) {
		this.file = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours/" + name + ".yml");
		if (file.exists()) throw new IllegalArgumentException("Parkour file already exists: " + file.getPath());
		this.file.mkdirs();

		this.name = name;
		this.spawnLocation = spawnLocation;
	}

	public Parkour(File file) {
		this.file = file;
		load(file);
	}

	public String getFormattedLocation() {
		return "(" + spawnLocation.getBlockX() + ", " + spawnLocation.getBlockY() + ", " + spawnLocation.getBlockZ() + ")";
	}

	public void load(File file) {
		if (!file.exists()) {
			MyParkour.INSTANCE.getLogger().warning("Parkour file not found: " + file.getPath());
			return;
		}

		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		this.name = config.getString("name");
		if (this.name == null) {
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
				System.out.println("Parkour spawnLocation.world not found in parkour: " + name);
				return;
			}

			this.spawnLocation = new Location(MyParkour.INSTANCE.getServer().getWorld(worldName), x, y, z);
		} else {
			System.out.println("Parkour spawnLocation not found in parkour: " + name);
			return;
		}

		ConfigurationSection leaderboardSection = config.getConfigurationSection("leaderboard");
		if (leaderboardSection != null) {
			for (String key : leaderboardSection.getKeys(false)) {
				String playerName = leaderboardSection.getString(key + ".playerName");
				long time = leaderboardSection.getLong(key + ".time");
				int jumps = leaderboardSection.getInt(key + ".jumps");
				int deaths = leaderboardSection.getInt(key + ".deaths");

				LeaderboardEntry entry = new LeaderboardEntry(playerName, time, jumps, deaths);
				this.leaderboard.add(entry);
			}
		} else {
			System.out.println("Parkour leaderboard not found in parkour: " + name);
		}
	}

	public void save() {
		File file = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours/" + name + ".yml");
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
		config.set("name", this.name);

		config.set("spawnLocation.x", this.spawnLocation.getX());
		config.set("spawnLocation.y", this.spawnLocation.getY());
		config.set("spawnLocation.z", this.spawnLocation.getZ());
		config.set("spawnLocation.world", this.spawnLocation.getWorld().getName());

		for (int i = 0; i < leaderboard.size(); i++) {
			LeaderboardEntry entry = leaderboard.get(i);
			config.set("leaderboard." + i + ".playerName", entry.playerName);
			config.set("leaderboard." + i + ".time", entry.time);
			config.set("leaderboard." + i + ".jumps", entry.jumps);
			config.set("leaderboard." + i + ".deaths", entry.deaths);
		}

		try {
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		File file = new File(MyParkour.INSTANCE.getDataFolder() + "/parkours/" + name + ".yml");
		if (file.exists()) file.delete();
	}
}
