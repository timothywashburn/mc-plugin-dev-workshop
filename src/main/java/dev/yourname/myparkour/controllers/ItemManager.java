package dev.yourname.myparkour.controllers;

import dev.yourname.myparkour.MyParkour;
import dev.yourname.myparkour.enums.CustomItemType;
import dev.yourname.myparkour.utils.ParkourUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

// TODO: step 14
public class ItemManager implements Listener {
	public static final NamespacedKey ID_KEY = new NamespacedKey(MyParkour.INSTANCE, "id");

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() == null) return;
		ItemStack itemStack = event.getItem();
		CustomItemType itemType = getCustomItemType(itemStack);
		switch (itemType) {
			case RESET_TO_CHECKPOINT -> { // TODO: step 15
				Location checkpoint = ParkourPlayerManager.getCheckpoint(player);
				if (checkpoint == null) {
					ParkourUtils.sendMessage(player, "&c&lERROR!&7 You have not reached a checkpoint yet!");
					return;
				}
				player.teleport(checkpoint);
			}
			case EXIT_PARKOUR -> ParkourPlayerManager.exitParkour(player);
		}
	}

	public static ItemStack createCustomItem(CustomItemType itemType) {
		switch (itemType) {
			case RESET_TO_CHECKPOINT -> { // TODO: step 15
				ItemStack itemStack = new ItemStack(Material.OAK_DOOR);

				itemStack.editMeta(itemMeta -> {
					itemMeta.setDisplayName(ParkourUtils.colorize("&aReset to Checkpoint"));
					itemMeta.lore(ParkourUtils.createLore(
							"Right-click to reset to the",
							"last checkpoint you reached"
					));

					itemMeta.getPersistentDataContainer().set(ID_KEY, PersistentDataType.STRING, itemType.getId());
				});

				return itemStack;
			}
			case EXIT_PARKOUR -> {
				ItemStack itemStack = new ItemStack(Material.BARRIER);

				itemStack.editMeta(itemMeta -> {
					itemMeta.setDisplayName(ParkourUtils.colorize("&cExit Parkour"));
					itemMeta.lore(ParkourUtils.createLore(
							"Right-click to exit parkour"
					));

					itemMeta.getPersistentDataContainer().set(ID_KEY, PersistentDataType.STRING, itemType.getId());
				});

				return itemStack;
			}
		}
		throw new IllegalArgumentException();
	}

	public static CustomItemType getCustomItemType(ItemStack itemStack) {
		if (itemStack == null || !itemStack.hasItemMeta()) return null;
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (!itemMeta.getPersistentDataContainer().has(ID_KEY)) return null;
		String id = itemMeta.getPersistentDataContainer().get(ID_KEY, PersistentDataType.STRING);
		for (CustomItemType itemType : CustomItemType.values()) if (itemType.getId().equals(id)) return itemType;
		return null;
	}
}
