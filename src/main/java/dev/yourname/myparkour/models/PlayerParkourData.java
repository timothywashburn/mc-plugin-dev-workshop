package dev.yourname.myparkour.models;

import org.bukkit.entity.Player;

public class PlayerParkourData {
	public Parkour parkour;
	public Player player;
	public boolean timerStarted;
	public boolean isComplete;

	public String playerName;
	public long ticks;
	public int jumps;
	public int deaths;

	/**
	 * Should be called when a player starts a parkour
	 * @param parkour The parkour the player is starting
	 * @param player The player starting the parkour
	 */
	public PlayerParkourData(Parkour parkour, Player player) {
		this(player.getName(), 0, 0, 0);
		this.parkour = parkour;
		this.player = player;
	}

	/**
	 * Should be called when loading a parkour data entry
	 * @param playerName The name of the player
	 * @param ticks The time in ticks
	 * @param jumps The number of jumps
	 * @param deaths The number of deaths
	 */
	public PlayerParkourData(String playerName, long ticks, int jumps, int deaths) {
		this.playerName = playerName;
		this.ticks = ticks;
		this.jumps = jumps;
		this.deaths = deaths;
	}

	public void reset() {
		this.ticks = 0;
		this.jumps = 0;
		this.deaths = 0;
	}
}
