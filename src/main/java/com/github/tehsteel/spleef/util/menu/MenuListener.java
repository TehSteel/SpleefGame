package com.github.tehsteel.spleef.util.menu;

import com.github.tehsteel.spleef.util.menu.model.Menu;
import com.github.tehsteel.spleef.util.menu.model.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class MenuListener implements Listener {

	@EventHandler
	public void onInventoryClickEvent(final InventoryClickEvent event) {
		final Player player = (Player) event.getView().getPlayer();
		final Menu menu = Menu.getOpenedMenus().get(player);

		if (menu == null) return;
		event.setCancelled(true);

		final MenuButton button = menu
				.getButtons()
				.stream()
				.filter(menuButtons -> menuButtons.getSlot() == event.getSlot())
				.findFirst()
				.orElse(null);
		if (button == null) return;
		button.onClickEvent(player, event);
		menu.setPlayer(null);
	}


	@EventHandler
	public void onInventoryCloseEvent(final InventoryCloseEvent event) {
		final Player player = (Player) event.getView().getPlayer();
		final Menu menu = Menu.getOpenedMenus().get(player);

		if (menu == null) return;

		menu.onPlayerClose(player);
		Menu.getOpenedMenus().remove(player);
		menu.setPlayer(null);
	}
}
