package dev.yourname.myparkour;

import dev.yourname.myparkour.commands.AdminCommand;
import dev.yourname.myparkour.commands.LeaderboardCommand;
import dev.yourname.myparkour.commands.ParkourCommand;
import dev.yourname.myparkour.controllers.ItemManager;
import dev.yourname.myparkour.controllers.ParkourManager;
import dev.yourname.myparkour.controllers.ParkourPlayerManager;
import dev.yourname.myparkour.controllers.PlayerManager;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyParkour extends JavaPlugin {
	public static MyParkour INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;
		System.out.println("hello world"); // TODO: step 0

		ParkourManager.init(); // TODO: step 1

		Bukkit.getServer().getPluginCommand("parkour").setExecutor(new ParkourCommand()); // TODO: step 1
		Bukkit.getServer().getPluginCommand("leaderboard").setExecutor(new LeaderboardCommand()); // TODO: step 10
		Bukkit.getServer().getPluginCommand("admin").setExecutor(new AdminCommand()); // TODO: step 11

		Bukkit.getPluginManager().registerEvents(new PlayerManager(), this); // TODO: step 1
		Bukkit.getPluginManager().registerEvents(new ItemManager(), this); // TODO: step 14
	}

	@Override
	public void onDisable() {
		ParkourPlayerManager.stopParkour(); // TODO: step 1
		for (Parkour parkour : ParkourManager.parkourList) parkour.save(); // TODO: step 2
	}
}
