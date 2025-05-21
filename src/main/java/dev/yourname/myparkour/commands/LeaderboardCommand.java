package dev.yourname.myparkour.commands;

import dev.yourname.myparkour.controllers.ParkourManager;
import dev.yourname.myparkour.enums.SortType;
import dev.yourname.myparkour.utils.ParkourUtils;
import dev.yourname.myparkour.models.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// TODO: step 10
public class LeaderboardCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
		if (!(sender instanceof Player player)) return false;

		if (args.length < 1) {
			ParkourUtils.sendMessage(player, "&c&lERROR!&7 Usage: /" + label + " <parkourName> [time|jumps|deaths]");
			return false;
		}

		String parkourName = args[0].toLowerCase();
		Parkour parkour = ParkourManager.getParkour(parkourName);
		if (parkour == null) {
			ParkourUtils.sendMessage(player, "&c&lERROR!&7 Parkour not found: " + parkourName);
			return false;
		}

		SortType sortType;
		if (args.length > 1) {
			String sortTypeString = args[1].toLowerCase();
			switch (sortTypeString) {
				case "time" -> sortType = SortType.FASTEST_TIME_FIRST;
				case "jumps" -> sortType = SortType.LOWEST_JUMPS_FIRST;
				case "deaths" -> sortType = SortType.LOWEST_DEATHS_FIRST;
				default -> {
					ParkourUtils.sendMessage(player, "&c&lERROR!&7 Invalid sort type: " + sortTypeString);
					return false;
				}
			}
		} else {
			sortType = SortType.FASTEST_TIME_FIRST;
		}

		parkour.printLeaderboard(player, sortType);
		return false;
	}
}
