package com.github.tehsteel.spleef.command;

import com.github.tehsteel.minigameapi.arena.ArenaException;
import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.arena.model.SpleefArena;
import com.github.tehsteel.spleef.command.model.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ArenaCommand extends BaseCommand {
	public ArenaCommand() {
		super("arena");

		setPermission("spleefgame.command.arena");
		permissionMessage(Constants.Messages.Main.NO_PERMISSION);
	}

	@Override
	protected void run(final CommandSender sender, final String[] args) {
		if (!(sender instanceof final Player player)) return;

		if (args.length == 0) {
			sendHelpMessage();
			return;
		}

		switch (args[0].toLowerCase()) {
			case "create" -> {
				final SpleefArena arena = plugin.getArenaManager().createArena(args[1]);
				try {
					arena.serialize();
					message(Constants.Messages.Arena.CREATE.replace("%arena_name%", arena.getName()));
				} catch (final ArenaException e) {
					throw new RuntimeException(e);
				}
			}

			case "delete" -> {
				final SpleefArena arena = plugin.getArenaManager().findArenaByName(args[1]);

				if (arena == null) {
					message(Constants.Messages.Arena.DOSENT_EXIST);
					return;
				}

				plugin.getArenaManager().deleteArena(arena);
				message(Constants.Messages.Arena.DELETE.replace("%arena_name%", arena.getName()));
			}

			case "setwaitinglocation" -> {
				final SpleefArena arena = plugin.getArenaManager().findArenaByName(args[1]);

				if (arena == null) {
					message(Constants.Messages.Arena.DOSENT_EXIST);
					return;
				}


				arena.setWaitingLocation(player.getLocation());
				try {
					arena.serialize();
				} catch (final ArenaException e) {
					throw new RuntimeException();
				}
				message(Constants.Messages.Arena.SET_WAITING_LOCATION.replace("%arena_name%", arena.getName()));
			}

			case "setmaxplayers" -> {
				final SpleefArena arena = plugin.getArenaManager().findArenaByName(args[1]);

				if (arena == null) {
					message(Constants.Messages.Arena.DOSENT_EXIST);
					return;
				}

				arena.setMaxPlayers(Integer.parseInt(args[2]));

				try {
					arena.serialize();
				} catch (final ArenaException e) {
					throw new RuntimeException();
				}
				message(Constants.Messages.Arena.SET_MAX_PLAYERS
						.replace("%arena_name%", arena.getName())
						.replace("%arena_max_players%", args[2])
				);
			}

			case "setminplayers" -> {
				final SpleefArena arena = plugin.getArenaManager().findArenaByName(args[1]);

				if (arena == null) {
					message(Constants.Messages.Arena.DOSENT_EXIST);
					return;
				}

				arena.setMinPlayers(Integer.parseInt(args[2]));

				try {
					arena.serialize();
				} catch (final ArenaException e) {
					throw new RuntimeException();
				}
				message(Constants.Messages.Arena.SET_MIN_PLAYERS
						.replace("%arena_name%", arena.getName())
						.replace("%arena_min_players%", args[2])
				);
			}

			case "addspawnlocation" -> {
				final SpleefArena arena = plugin.getArenaManager().findArenaByName(args[1]);

				if (arena == null) {
					message(Constants.Messages.Arena.DOSENT_EXIST);
					return;
				}


				arena.getSpawnLocations().add(player.getLocation());
				try {
					arena.serialize();
				} catch (final ArenaException e) {
					throw new RuntimeException();
				}
				message(Constants.Messages.Arena.ADD_SPAWN_LOCATION.replace("%arena_name%", arena.getName()));
			}

			case "setminy" -> {
				final SpleefArena arena = plugin.getArenaManager().findArenaByName(args[1]);

				if (arena == null) {
					message(Constants.Messages.Arena.DOSENT_EXIST);
					return;
				}

				arena.setMinY(Double.parseDouble(args[2]));

				try {
					arena.serialize();
				} catch (final ArenaException e) {
					throw new RuntimeException();
				}
				message(Constants.Messages.Arena.SET_MIN_Y
						.replace("%arena_name%", arena.getName())
						.replace("%arena_min_y%", args[2])
				);
			}
		}
	}

	private void sendHelpMessage() {
		Constants.Messages.Arena.HELP_MESSAGE.forEach(this::message);
	}

}
