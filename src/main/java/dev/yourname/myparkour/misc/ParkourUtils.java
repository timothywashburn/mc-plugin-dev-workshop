package dev.yourname.myparkour.misc;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
		return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds / 10);
	}

	public static List<? extends Component> createLore(String... lines) {
		List<Component> lore = new ArrayList<>();
		for (String line : lines) lore.add(Component.text(colorize("&7" + line)));
		return lore;
	}

	public static Color getRainbowColor(int hue) {
		float saturation = 1.0f;
		float value = 1.0f;

		float c = value * saturation;
		float x = c * (1 - Math.abs((hue / 60.0f) % 2 - 1));
		float m = value - c;

		float r, g, b;

		if (hue < 60) {
			r = c; g = x; b = 0;
		} else if (hue < 120) {
			r = x; g = c; b = 0;
		} else if (hue < 180) {
			r = 0; g = c; b = x;
		} else if (hue < 240) {
			r = 0; g = x; b = c;
		} else if (hue < 300) {
			r = x; g = 0; b = c;
		} else {
			r = c; g = 0; b = x;
		}

		int red = Math.round((r + m) * 255);
		int green = Math.round((g + m) * 255);
		int blue = Math.round((b + m) * 255);

		return Color.fromRGB(red, green, blue);
	}
}
