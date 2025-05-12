package dev.yourname.myparkour;

import dev.yourname.myparkour.commands.AdminCommand;
import dev.yourname.myparkour.commands.LeaderboardCommand;
import dev.yourname.myparkour.commands.ParkourCommand;
import dev.yourname.myparkour.controllers.ItemManager;
import dev.yourname.myparkour.controllers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyParkour extends JavaPlugin {
	public static MyParkour INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;
		System.out.println("hello world");

		Bukkit.getServer().getPluginCommand("parkour").setExecutor(new ParkourCommand());
		Bukkit.getServer().getPluginCommand("leaderboard").setExecutor(new LeaderboardCommand());
		Bukkit.getServer().getPluginCommand("admin").setExecutor(new AdminCommand());

		Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
		Bukkit.getPluginManager().registerEvents(new ItemManager(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
