package com.github.tehsteel.spleef.command;

import com.github.tehsteel.spleef.command.model.BaseCommand;
import com.github.tehsteel.spleef.menu.StatsMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class StatsCommand extends BaseCommand {
	public StatsCommand() {
		super("stats");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (!(sender instanceof final Player player)) return;
		new StatsMenu().openMenu(player);
	}
}
