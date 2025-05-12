package dev.yourname.myparkour.models;

import dev.yourname.myparkour.controllers.ParkourDataManager;
import dev.yourname.myparkour.enums.SortType;
import dev.yourname.myparkour.misc.ParkourUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

		ParkourDataManager.createParkourFile(this);
		save();
	}

	public Parkour(File file) {
		this.file = file;
		load();
	}

	public List<PlayerParkourData> getSortedLeaderboard(SortType sortType, int size) {
		List<PlayerParkourData> sortedLeaderboard = new ArrayList<>(leaderboard);
		sortedLeaderboard.sort((data1, data2) -> switch (sortType) {
			case FASTEST_TIME_FIRST -> Long.compare(data1.ticks, data2.ticks);
			case LOWEST_JUMPS_FIRST -> Integer.compare(data1.jumps, data2.jumps);
			case LOWEST_DEATHS_FIRST -> Integer.compare(data1.deaths, data2.deaths);
		});
		return sortedLeaderboard.size() > size ? sortedLeaderboard.subList(0, size) : sortedLeaderboard;
	}

	public void printLeaderboard(Player player, SortType sortType) {
		List<PlayerParkourData> sortedLeaderboard = getSortedLeaderboard(sortType, 10);
		if (sortedLeaderboard.isEmpty()) {
			ParkourUtils.sendMessage(player, "&c&lSORRY!&7 No players have completed this parkour yet!");
			return;
		}

		String header = ParkourUtils.createHeader(ChatColor.GREEN, ChatColor.DARK_GREEN, 10, name + " lb");
		ParkourUtils.sendMessage(player, header);
		for (int i = 0; i < sortedLeaderboard.size(); i++) {
			PlayerParkourData data = sortedLeaderboard.get(i);
			String prefixColor = switch (i) {
				case 0 -> "&e";
				case 1 -> "&f";
				case 2 -> "&6";
				default -> "&7";
			};
			String message = prefixColor + (i + 1) + ". &2" + data.playerName + "&7: " +
					"&a" + ParkourUtils.getFormattedTicks(data.ticks) + "&7, " +
					"&a" + data.jumps + "&7 jump" + (data.jumps == 1 ? "" : "s") + ", " +
					"&a" + data.deaths + "&7 death" + (data.deaths == 1 ? "" : "s");
			ParkourUtils.sendMessage(player, message);
		}
		ParkourUtils.sendMessage(player, header);
	}

	public void clearLeaderboard() {
		leaderboard.clear();
		save();
	}

	public String getFormattedLocation() {
		return "(" + spawnLocation.getBlockX() + ", " + spawnLocation.getBlockY() + ", " + spawnLocation.getBlockZ() + ")";
	}

	public void load() {
		ParkourDataManager.loadParkourData(this);
	}

	public void save() {
		ParkourDataManager.save(this);
	}

	public void delete() {
		ParkourDataManager.delete(this);
	}
}
