package dev.yourname.myparkour.controllers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerManager implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
	}

	@EventHandler
	public void onPlayerJump(PlayerJumpEvent event) {
		Player player = event.getPlayer();

	}
}
