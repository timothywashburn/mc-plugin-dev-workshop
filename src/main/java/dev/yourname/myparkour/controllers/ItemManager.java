package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.enums.CustomItemType;
import dev.yourname.myparkour.misc.ParkourUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemManager implements Listener {

	public static ItemStack createCustomItem(CustomItemType itemType) {
		switch (itemType) {
			case RESET_TO_CHECKPOINT -> {
				ItemStack itemStack = new ItemStack(Material.OAK_DOOR);

				itemStack.editMeta(itemMeta -> {
					itemMeta.setDisplayName(ParkourUtils.colorize("&aReset to Checkpoint"));
					itemMeta.lore(ParkourUtils.createLore(
							"Right-click to reset to the",
							"last checkpoint you reached"
					));

					itemMeta.getPersistentDataContainer().set(new NamespacedKey(MyParkour.INSTANCE, "id"),
							PersistentDataType.STRING, itemType.getId());
				});

				return itemStack;
			}
			case EXIT_PARKOUR -> {
				return new ItemStack(Material.BARRIER);
			}
		}
		throw new IllegalArgumentException();
	}

	public static boolean isCustomItem(CustomItemType itemType) {
		return false;
	}
}
