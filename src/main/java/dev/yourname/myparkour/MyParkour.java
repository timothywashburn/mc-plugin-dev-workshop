package dev.yourname.myparkour;

import org.bukkit.plugin.java.JavaPlugin;

public final class MyParkour extends JavaPlugin {
	public static MyParkour INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;
		System.out.println("hello world");
	}

	@Override
	public void onDisable() {
	}
}
