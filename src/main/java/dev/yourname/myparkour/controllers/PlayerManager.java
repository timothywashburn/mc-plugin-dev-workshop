package dev.yourname.myparkour.controllers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.models.PlayerParkourData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerManager implements Listener {

	public PlayerManager() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!ParkourPlayerManager.isParkouring(player)) continue;
					PlayerParkourData data = ParkourPlayerManager.getParkourData(player);
					data.ticks++;
				}
			}
		}.runTaskTimer(MyParkour.INSTANCE, 0L, 1L);
	}

	public void handlePlayerPositionChange(Player player) {
		if (!ParkourPlayerManager.isParkouring(player)) return;
		PlayerParkourData data = ParkourPlayerManager.getParkourData(player);

		double distanceFallen = player.getLocation().getY() - data.parkour.spawnLocation.getY();
		if (distanceFallen > 20) {
			data.deaths++;
			// TODO: better death handling?
			player.teleport(data.parkour.spawnLocation);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		handlePlayerPositionChange(event.getPlayer());
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		handlePlayerPositionChange(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!ParkourPlayerManager.isParkouring(player)) return;
		ParkourPlayerManager.endParkour(player);
	}

	@EventHandler
	public void onPlayerJump(PlayerJumpEvent event) {
		Player player = event.getPlayer();
		if (!ParkourPlayerManager.isParkouring(player)) return;
		PlayerParkourData data = ParkourPlayerManager.getParkourData(player);
		data.jumps++;
	}

	@EventHandler
	public void onHungerLoss(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player player)) return;
		if (ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerHurt(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) return;
		if (ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if (!ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player player)) return;
		if (!ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemDrag(InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player player)) return;
		if (!ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}
}
