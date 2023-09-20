package com.github.tehsteel.spleef.util.menu.model;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Menu {


	@Getter private static final Map<Player, Menu> openedMenus = new HashMap<>();

	@Getter @Setter private Player player;

	public static final void updateMenu(final Player player) {
		final Menu menu = openedMenus.get(player);
		if (menu == null) return;
		final Inventory inventory = player.getOpenInventory().getTopInventory();

		for (int i = 0; i < menu.getButtons().size(); i++) {
			inventory.setItem(menu.getButtons().get(i).getSlot(), menu.getButtons().get(i).getItem());
		}

		if (menu.getFilledItem() != null) {
			for (int i = 0; i < inventory.getSize(); i++) {
				if (inventory.getItem(i) == null)
					inventory.setItem(i, menu.getFilledItem());
			}
		}

		player.updateInventory();
		menu.setPlayer(player);
	}

	public abstract String getTitle();

	public abstract int getMenuSize();

	public ItemStack getFilledItem() {
		return null;
	}

	public abstract List<MenuButton> getButtons();

	public void onPlayerClose(final Player player) {
		this.player = null;
	}

	public void openMenu(final Player player) {
		this.player = player;
		final Inventory inventory = Bukkit.createInventory(player, getMenuSize(), MiniMessage.miniMessage().deserialize(getTitle()));


		for (int i = 0; i < getButtons().size(); i++) {
			inventory.setItem(getButtons().get(i).getSlot(), getButtons().get(i).getItem());
		}

		if (getFilledItem() != null) {
			for (int i = 0; i < inventory.getSize(); i++) {
				if (inventory.getItem(i) == null)
					inventory.setItem(i, getFilledItem());
			}
		}

		player.openInventory(inventory);
		openedMenus.put(player, this);
	}


}
