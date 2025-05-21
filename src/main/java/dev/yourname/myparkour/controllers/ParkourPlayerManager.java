package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.enums.CustomItemType;
import dev.yourname.myparkour.utils.ParkourUtils;
import dev.yourname.myparkour.models.Parkour;
import dev.yourname.myparkour.models.PlayerParkourData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

// TODO: step 1
public class ParkourPlayerManager {
	private static final Map<Player, PlayerParkourData> parkourPlayers = new HashMap<>();
	private static final Map<Player, Location> checkpointMap = new HashMap<>(); // TODO: step 5

	public static void startParkour(Player player, Parkour parkour) {
		if (isParkouring(player)) throw new IllegalArgumentException("Player is already parkouring.");

		PlayerParkourData parkourData = new PlayerParkourData(parkour, player);
		parkour.leaderboard.add(parkourData);

		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.setFlying(false);

		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.getInventory().setItem(4, ItemManager.createCustomItem(CustomItemType.RESET_TO_CHECKPOINT)); // TODO: step 14
		player.getInventory().setItem(8, ItemManager.createCustomItem(CustomItemType.EXIT_PARKOUR)); // TODO: step 15

		player.teleport(parkour.spawnLocation);

		parkourPlayers.put(player, parkourData);
	}

	public static void exitParkour(Player player) {
		parkourPlayers.remove(player);
		checkpointMap.remove(player); // TODO: step 5

		if (!player.isOnline()) return;

		player.setGameMode(GameMode.SURVIVAL);

		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);

		ParkourUtils.sendMessage(player, "&c&lERROR!&7 You have exited parkour mode!");
	}

	public static void stopParkour() {
		for (Player player : parkourPlayers.keySet()) exitParkour(player);
	}

	public static void stopParkour(Parkour parkour) {
		for (Player player : parkourPlayers.keySet()) {
			PlayerParkourData data = parkourPlayers.get(player);
			if (data.parkour.equals(parkour)) exitParkour(player);
		}
	}

	public static void completeParkour(Player player) {
		PlayerParkourData data = parkourPlayers.get(player);
		data.isComplete = true;
		data.parkour.save();

		player.setGameMode(GameMode.SURVIVAL);
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
		ParkourUtils.sendMessage(player, "&a&lERROR!&7 You have completed the parkour in " +
				ParkourUtils.getFormattedTicks(data.ticks) + "!");

		parkourPlayers.remove(player);
		checkpointMap.remove(player); // TODO: step 5
	}

	public static boolean isParkouring(Player player) {
		return parkourPlayers.containsKey(player);
	}

	public static PlayerParkourData getParkourData(Player player) {
		return parkourPlayers.get(player);
	}

	// TODO: step 5
	public static Location getCheckpoint(Player player) {
		return checkpointMap.get(player);
	}

	// TODO: step 5
	public static void setCheckpoint(Player player, Location checkpoint) {
		checkpointMap.put(player, checkpoint);
	}
}
