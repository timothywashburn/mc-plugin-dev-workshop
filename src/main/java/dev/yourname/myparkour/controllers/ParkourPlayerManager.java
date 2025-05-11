package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.models.Parkour;
import dev.yourname.myparkour.models.PlayerParkourData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ParkourPlayerManager {
	private static final Map<Player, PlayerParkourData> parkourPlayers = new HashMap<>();

	public static void startParkour(Player player, Parkour parkour) {
		if (isParkouring(player)) throw new IllegalArgumentException("Player is already parkouring.");
		PlayerParkourData parkourData = new PlayerParkourData(parkour, player);

		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.setFlying(false);

		parkourPlayers.put(player, parkourData);
	}

	public static void endParkour(Player player) {
		parkourPlayers.remove(player);

		if (!player.isOnline()) return;

		player.setGameMode(GameMode.SURVIVAL);
	}

	public static boolean isParkouring(Player player) {
		return parkourPlayers.containsKey(player);
	}

	public static PlayerParkourData getParkourData(Player player) {
		return parkourPlayers.get(player);
	}
}
