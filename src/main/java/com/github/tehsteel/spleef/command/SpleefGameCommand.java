package com.github.tehsteel.spleef.command;

import com.github.tehsteel.minigameapi.util.CustomLocation;
import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.command.model.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public final class SpleefGameCommand extends BaseCommand {
	public SpleefGameCommand() {
		super("spleefgame");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (consoleCheck()) return;
		final Player player = (Player) sender;

		switch (args[0].toLowerCase()) {
			case "reload" -> {
				SpleefPlugin.getInstance().getMessagesConfig().reloadConfig();
				Constants.loadConstants();
			}

			case "setspawn" -> {
				SpleefPlugin.getInstance().getSettingsConfig().getConfig().set("spawnLocation", CustomLocation.fromBukkitLocation(player.getLocation()).serialize());
				try {
					SpleefPlugin.getInstance().getSettingsConfig().saveConfig();
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
