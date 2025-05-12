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

	public PlayerParkourData(Parkour parkour, Player player) {
		this(player.getName(), 0, 0, 0);
		this.parkour = parkour;
		this.player = player;
	}

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
