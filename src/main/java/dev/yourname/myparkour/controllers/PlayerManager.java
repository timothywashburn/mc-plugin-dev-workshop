package dev.yourname.myparkour.controllers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.utils.ParkourUtils;
import dev.yourname.myparkour.models.PlayerParkourData;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerManager implements Listener {

	public PlayerManager() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!ParkourPlayerManager.isParkouring(player)) continue;
					PlayerParkourData data = ParkourPlayerManager.getParkourData(player);
					if (!data.timerStarted) continue;

					Color rainbowColor = ParkourUtils.getRainbowColor((int) (data.ticks % 360));
					Particle.DustOptions dustOptions = new Particle.DustOptions(
							rainbowColor, 1.5f
					);
					player.spawnParticle(Particle.DUST, player.getLocation(), 3, dustOptions);

					player.sendActionBar(Component.text(ParkourUtils.colorize(
							"&2&lPARKOUR!&a " + data.parkour.name +
									" &7| &a" + ParkourUtils.getFormattedTicks(data.ticks) +
									" &7| &a" + data.jumps + " jump" + (data.jumps == 1 ? "" : "s") +
									" &7| &a" + data.deaths + " death" + (data.deaths == 1 ? "" : "s")
					)));

					data.ticks++;
				}
			}
		}.runTaskTimer(MyParkour.INSTANCE, 0L, 1L);
	}

	public void handlePlayerPositionChange(Player player) {
		if (!ParkourPlayerManager.isParkouring(player)) return;
		PlayerParkourData data = ParkourPlayerManager.getParkourData(player);

		double distanceFallen = data.parkour.spawnLocation.getY() - player.getLocation().getY();
		if (distanceFallen > 20) {
			data.deaths++;
			Location respawnLocation = ParkourPlayerManager.getCheckpoint(player);
			if (respawnLocation == null) respawnLocation = data.parkour.spawnLocation;
			player.teleport(respawnLocation);
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
	public void onPressurePlate(PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL) return;
		Block block = event.getClickedBlock();
		if (block == null || block.getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) return;
		Player player = event.getPlayer();

		Block blockUnder = block.getRelative(0, -1, 0);
		if (blockUnder.getType() == Material.IRON_BLOCK) {
			if (!ParkourPlayerManager.isParkouring(player)) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Use /parkour start <parkourName> to start parkouring!");
				return;
			}

			PlayerParkourData data = ParkourPlayerManager.getParkourData(player);
			data.reset();
			data.timerStarted = true;
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);

		} else if (blockUnder.getType() == Material.GOLD_BLOCK) {
			if (!ParkourPlayerManager.isParkouring(player)) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Use /parkour start <parkourName> to start parkouring!");
				return;
			}

			PlayerParkourData data = ParkourPlayerManager.getParkourData(player);
			if (!data.timerStarted) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 You skipped the start of the parkour!");
				return;
			}

			Location checkpointLocation = block.getLocation().add(0.5, 0, 0.5);
			checkpointLocation.setYaw(player.getYaw());
			checkpointLocation.setPitch(player.getPitch());
			ParkourPlayerManager.setCheckpoint(player, checkpointLocation);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
			ParkourUtils.sendMessage(player, "&6&lCHECKPOINT!&7 You reached a checkpoint!");

		} else if (blockUnder.getType() == Material.EMERALD_BLOCK) {
			if (!ParkourPlayerManager.isParkouring(player)) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Use /parkour start <parkourName> to start parkouring!");
				return;
			}

			PlayerParkourData data = ParkourPlayerManager.getParkourData(player);
			if (!data.timerStarted) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 You skipped the start of the parkour!");
				return;
			}

			ParkourPlayerManager.completeParkour(player);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!ParkourPlayerManager.isParkouring(player)) return;
		ParkourPlayerManager.exitParkour(player);
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
		if (!ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerHurt(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) return;
		if (!ParkourPlayerManager.isParkouring(player)) return;
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

	@EventHandler
	public void onItemPickup(PlayerAttemptPickupItemEvent event) {
		Player player = event.getPlayer();
		if (!ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (!ParkourPlayerManager.isParkouring(player)) return;
		event.setCancelled(true);
	}
}