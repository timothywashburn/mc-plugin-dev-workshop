package dev.yourname.myparkour.misc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ParkourUtils {
	public static String createHeader(ChatColor primary, ChatColor secondary, int size, String centerText) {
		String primaryString = "&" + primary.getChar();
		String secondaryString = "&" + secondary.getChar();

		String header = secondaryString +
				"&l" +
				"&m ".repeat(Math.max(0, size)) +
				secondaryString +
				"&l<" +
				primaryString +
				"&l " +
				centerText.toUpperCase() +
				secondaryString +
				"&l >" +
				"&m ".repeat(Math.max(0, size));

		return colorize(header);
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message).replaceAll("&&", "&");
	}

	public static void sendMessage(Player player, String message) {
		player.sendMessage(colorize(message));
	}

	public static String getFormattedTicks(long ticks) {
		long seconds = ticks / 20;
		long minutes = seconds / 60;
		seconds %= 60;
		long milliseconds = (ticks % 20) * 50;
		return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);
	}
}
