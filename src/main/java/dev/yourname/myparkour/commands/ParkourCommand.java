package dev.yourname.myparkour.commands;

import dev.yourname.myparkour.controllers.ParkourManager;
import dev.yourname.myparkour.controllers.ParkourPlayerManager;
import dev.yourname.myparkour.misc.ParkourUtils;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParkourCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
		if (!(sender instanceof Player player)) return false;

		if (args.length < 1) {
			ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " <start|list|create|delete>");
			return false;
		}

		String subCommand = args[0].toLowerCase();

		if (subCommand.equalsIgnoreCase("start")) {
			if (ParkourPlayerManager.isParkouring(player)) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 You are already in a parkour!");
				return false;
			}

			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " start <parkourName>");
				return false;
			}

			String parkourName = args[1].toLowerCase();
			Parkour parkour = ParkourManager.getParkour(parkourName);
			if (parkour == null) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour not found: " + parkourName);
				return false;
			}

			ParkourPlayerManager.startParkour(player, parkour);

		} else if (subCommand.equalsIgnoreCase("list")) {
			String header = ParkourUtils.createHeader(ChatColor.GREEN, ChatColor.DARK_GREEN, 10, "parkours");
			ParkourUtils.sendMessage(player, header);
			for (Parkour parkour : ParkourManager.parkourList) {
				String worldName = parkour.spawnLocation.getWorld().getName();
				String message = "&a> " + parkour.name + " &7" + worldName + " " + parkour.getFormattedLocation();
				ParkourUtils.sendMessage(player, message);
			}
			ParkourUtils.sendMessage(player, header);

		} else if (subCommand.equalsIgnoreCase("create")) {
			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " create <parkourName>");
				return false;
			}

			String parkourName = args[1].toLowerCase();
			if (ParkourManager.getParkour(parkourName) != null) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour already exists: " + parkourName);
				return false;
			}
			if (!parkourName.matches("[a-zA-Z0-9_ ]+")) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour name can only contain letters, numbers, underscores, and spaces.");
				return false;
			}
			if (parkourName.length() > 20) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour name cannot be longer than 20 characters.");
				return false;
			}

			ParkourManager.createParkour(parkourName, player.getLocation());
			ParkourUtils.sendMessage(player, "&a&lSUCCESS!&7 Created parkour: " + parkourName);

		} else if (subCommand.equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " delete <parkourName>");
				return false;
			}

			String parkourName = args[1].toLowerCase();
			Parkour parkour = ParkourManager.getParkour(parkourName);
			if (parkour == null) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour not found: " + parkourName);
				return false;
			}

			ParkourManager.deleteParkour(parkourName);
			ParkourUtils.sendMessage(player, "&a&lSUCCESS!&7 Deleted parkour: " + parkourName);

		} else {
			ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " <start|list|create|delete>");
		}
		return false;
	}
}
