package dev.yourname.myparkour;

import dev.yourname.myparkour.commands.ParkourCommand;
import dev.yourname.myparkour.controllers.ParkourManager;
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

		Bukkit.getPluginManager().registerEvents(new ParkourManager(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerManager(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
