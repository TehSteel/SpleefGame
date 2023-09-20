package com.github.tehsteel.spleef.command;

import com.github.tehsteel.minigameapi.game.GameState;
import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.command.model.BaseCommand;
import com.github.tehsteel.spleef.game.model.SpleefGame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class GameCommand extends BaseCommand {
	public GameCommand() {
		super("game");
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (consoleCheck()) return;
		final Player player = (Player) sender;
		final SpleefGame game = plugin.getGameManager().findGameByPlayer(player);

		if (game == null) {
			message(Constants.Messages.Game.NOT_IN_GAME);
			return;
		}

		if (args.length == 0) {
			return;
		}


		switch (args[0].toLowerCase()) {
			case "leave" -> {
				game.removePlayer(player);
			}

			case "forcestart" -> {
				if (!player.hasPermission("spleefgame.command.forcestart")) {
					message(Constants.Messages.Main.NO_PERMISSION);
					return;
				}
				if (game.getState() != GameState.WAITING) return;
				game.startCountdown(true);
				message(Constants.Messages.Game.FORCESTART);
			}

			case "forceend" -> {
				if (!player.hasPermission("spleefgame.command.forceend")) {
					message(Constants.Messages.Main.NO_PERMISSION);
					return;
				}
				game.endGame(true);
			}

		}


	}
}
