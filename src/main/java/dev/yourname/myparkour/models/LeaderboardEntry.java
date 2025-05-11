package dev.yourname.myparkour.models;

public class LeaderboardEntry {
	public String playerName;
	public long time;
	public int jumps;
	public int deaths;

	public LeaderboardEntry(String playerName) {
		this(playerName, 0, 0, 0);
	}

	public LeaderboardEntry(String playerName, long time, int jumps, int deaths) {
		this.playerName = playerName;
		this.time = time;
		this.jumps = jumps;
		this.deaths = deaths;
	}
}
