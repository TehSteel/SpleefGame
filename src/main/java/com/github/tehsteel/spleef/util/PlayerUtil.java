package com.github.tehsteel.spleef.util;

import com.github.tehsteel.spleef.Constants;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PlayerUtil {


	private PlayerUtil() {
	}

	public static void message(final Player player, final String message) {
		player.sendMessage(MiniMessage.miniMessage().deserialize(message));
	}

	public static void message(final Player player, final String message, final Object... replace) {
		message(player, String.format(message, replace));
	}

	public static void message(final CommandSender sender, final String message) {
		sender.sendMessage(MiniMessage.miniMessage().deserialize(Constants.Messages.Main.PREFIX + " " + message));
	}

	public static void message(final CommandSender sender, final String message, final Object... replace) {
		message(sender, String.format(message, replace));
	}

	public static void clear(final Player player) {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
		player.setGameMode(GameMode.SURVIVAL);
		player.setFoodLevel(20);
		player.setHealth(20.0D);
	}
}
