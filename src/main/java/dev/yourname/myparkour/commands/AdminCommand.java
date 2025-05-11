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

public class AdminCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
		if (!(sender instanceof Player player)) return false;

		if (args.length < 1) {
			ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " <clearleaderboard>");
			return false;
		}

		String subCommand = args[0].toLowerCase();

		if (subCommand.equalsIgnoreCase("clearleaderboard")) {
			if (args.length < 2) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " clearleaderboard <parkourName>");
				return false;
			}

			String parkourName = args[1].toLowerCase();
			Parkour parkour = ParkourManager.getParkour(parkourName);
			if (parkour == null) {
				ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour not found: " + parkourName);
				return false;
			}

			parkour.clearLeaderboard();
			ParkourUtils.sendMessage(player, "&a&lSUCCESS!&7 Cleared leaderboard for parkour: " + parkourName);
		} else {
			ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " <clearleaderboard>");
		}
		return false;
	}
}
