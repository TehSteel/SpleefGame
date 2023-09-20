package com.github.tehsteel.spleef.menu;

import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.util.ItemCreator;
import com.github.tehsteel.spleef.util.menu.model.Menu;
import com.github.tehsteel.spleef.util.menu.model.MenuButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class StatsMenu extends Menu {
	private static final SpleefPlugin PLUGIN = SpleefPlugin.getInstance();

	@Override
	public String getTitle() {
		return Constants.Messages.Stats.MENU_TITLE.replace("%player_name%", getPlayer().getName());
	}

	@Override
	public int getMenuSize() {
		return 18;
	}

	@Override
	public List<MenuButton> getButtons() {
		return Arrays.asList(
				new WinsButton(getPlayer()),
				new LossesButton(getPlayer()),
				new CoinsButton(getPlayer()),
				new ExitButton()
		);
	}

	@RequiredArgsConstructor
	private static class WinsButton implements MenuButton {
		private final Player player;


		@Override
		public int getSlot() {
			return 11;
		}

		@Override
		public ItemStack getItem() {
			return ItemCreator.of(Material.TOTEM_OF_UNDYING)
					.setDisplayName("<green>You have won " + PLUGIN.getPlayerManager().getPlayerDataMap().get(player.getUniqueId()).getWins() + " games", false)
					.build();
		}

		@Override
		public void onClickEvent(final Player player, final InventoryClickEvent event) {

		}
	}

	@RequiredArgsConstructor
	private static class LossesButton implements MenuButton {
		private final Player player;


		@Override
		public int getSlot() {
			return 15;
		}

		@Override
		public ItemStack getItem() {
			return ItemCreator.of(Material.REDSTONE)
					.setDisplayName("<red>You have lost " + PLUGIN.getPlayerManager().getPlayerDataMap().get(player.getUniqueId()).getLosses() + " games", false)
					.build();
		}

		@Override
		public void onClickEvent(final Player player, final InventoryClickEvent event) {

		}
	}

	@RequiredArgsConstructor
	private static class ExitButton implements MenuButton {


		@Override
		public int getSlot() {
			return 22;
		}

		@Override
		public ItemStack getItem() {
			return ItemCreator.of(Material.BARRIER)
					.setDisplayName("<bold><red>Exit", false)
					.build();
		}

		@Override
		public void onClickEvent(final Player player, final InventoryClickEvent event) {
			player.closeInventory();
		}
	}

	@RequiredArgsConstructor
	private static class CoinsButton implements MenuButton {

		private final Player player;

		@Override
		public int getSlot() {
			return 13;
		}

		@Override
		public ItemStack getItem() {
			return ItemCreator.of(Material.GOLD_INGOT)
					.setDisplayName("<gold>You have " + PLUGIN.getPlayerManager().getPlayerDataMap().get(player.getUniqueId()).getCoins() + " coins", false)
					.build();
		}

		@Override
		public void onClickEvent(final Player player, final InventoryClickEvent event) {

		}
	}
}
