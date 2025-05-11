package dev.yourname.myparkour.commands;

import dev.yourname.myparkour.controllers.ParkourManager;
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
			ParkourUtils.sendMessage(player, "Usage: /" + label + " <start|list|create|delete>");
			return false;
		}

		String subCommand = args[0].toLowerCase();

		if (subCommand.equalsIgnoreCase("start")) {
			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "Usage: /" + label + " start <parkourName>");
				return false;
			}
		} else if (subCommand.equalsIgnoreCase("list")) {
			String header = ParkourUtils.createHeader(ChatColor.GREEN, ChatColor.DARK_GREEN, 10, "parkours");
			ParkourUtils.sendMessage(player, header);
			for (Parkour parkour : ParkourManager.parkours) {
				String worldName = parkour.spawnLocation.getWorld().getName();
				String message = "&a> " + parkour.name + " &7" + worldName + " " + parkour.getFormattedLocation();
				ParkourUtils.sendMessage(player, message);
			}
			ParkourUtils.sendMessage(player, header);
		} else if (subCommand.equalsIgnoreCase("create")) {
			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "Usage: /" + label + " create <parkourName>");
				return false;
			}
		} else if (subCommand.equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "Usage: /" + label + " delete <parkourName>");
				return false;
			}
		} else {
			ParkourUtils.sendMessage(player, "Usage: /" + label + " <start|list|create|delete>");
		}
		return false;
	}
}
