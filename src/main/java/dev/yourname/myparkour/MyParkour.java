package dev.yourname.myparkour;

import dev.yourname.myparkour.commands.ParkourCommand;
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

		Bukkit.getPluginManager().registerEvents(new PlayerManager(), this); // TODO: step 1
	}

	@Override
	public void onDisable() {
		ParkourPlayerManager.stopParkour(); // TODO: step 1
		for (Parkour parkour : ParkourManager.parkourList) parkour.save(); // TODO: step 2
	}
}
