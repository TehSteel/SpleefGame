package com.github.tehsteel.spleef.util.menu.model;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuButton {

	int getSlot();

	ItemStack getItem();

	void onClickEvent(Player player, InventoryClickEvent event);
}
