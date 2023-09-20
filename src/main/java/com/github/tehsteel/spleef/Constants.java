package com.github.tehsteel.spleef;

import com.github.tehsteel.minigameapi.util.CustomLocation;
import com.github.tehsteel.spleef.util.ItemCreator;
import com.github.tehsteel.spleef.util.SpleefItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Constants {

	public static final String PLUGIN_NAME = SpleefPlugin.getInstance().getName();


	private Constants() {
	}

	public static void loadConstants() {

		final FileConfiguration settingsCon = SpleefPlugin.getInstance().getSettingsConfig().getConfig();

		final FileConfiguration messagesCon = SpleefPlugin.getInstance().getMessagesConfig().getConfig();

		// Setup Config
		if (settingsCon.getString("spawnLocation") != null)
			Settings.SPAWN_LOCATION = CustomLocation.deserialize(Objects.requireNonNull(settingsCon.getString("spawnLocation"))).toBukkitLocation();
		else
			Settings.SPAWN_LOCATION = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();

		if (settingsCon.getString("Items.Shovel.Item") != null)
			Settings.SHOVEL_ITEM = new SpleefItem(ItemCreator.of(Material.valueOf(settingsCon.getString("Items.Shovel.Item"))).setDisplayName(settingsCon.getString("Items.Shovel.DisplayName")).build(), settingsCon.getInt("Items.Shovel.Slot", 0));

		if (settingsCon.getString("Items.QuickPlay.Item") != null)
			Settings.COMPASS_ITEM = new SpleefItem(ItemCreator.of(Material.valueOf(settingsCon.getString("Items.QuickPlay.Item"))).setDisplayName(settingsCon.getString("Items.QuickPlay.DisplayName")).build(), settingsCon.getInt("Items.QuickPlay.Slot", 0));

		// Setup main locale
		Arrays.asList(Constants.Messages.Main.class.getFields()).forEach(field -> {
			try {
				field.set(String.class, messagesCon.getString("MAIN." + field.getName()));
			} catch (final IllegalAccessException ignore) {
			}
		});

		// Setup arenas locale
		Arrays.asList(Constants.Messages.Arena.class.getFields()).forEach(field -> {
			if (!field.getName().equalsIgnoreCase("HELP_MESSAGE")) {
				try {
					field.set(String.class, messagesCon.getString("ARENA." + field.getName()));
				} catch (final IllegalAccessException ignore) {
				}
			}
		});
	}

	public static class Settings {
		public static Location SPAWN_LOCATION = null;
		public static SpleefItem SHOVEL_ITEM = new SpleefItem(ItemCreator.of(Material.NETHERITE_SHOVEL).build(), 0);
		public static SpleefItem COMPASS_ITEM = new SpleefItem(ItemCreator.of(Material.COMPASS).build(), 0);
	}

	public static class ArenaSettings {
		public static int MIN_PLAYERS = 4;
		public static int MAX_PLAYERS = 16;
	}

	public static class Messages {
		public static class Main {
			public static String PREFIX = "<gray>[<aqua>SpleefGame<gray>]";
			public static String NO_PERMISSION = "";
		}

		public static class Arena {
			public static List<String> HELP_MESSAGE = Arrays.asList(
					"&m&7-----------------------------------",
					"",
					"&m&7-----------------------------------"
			);

			public static String DOSENT_EXIST = "<red>That arena doesn't exist!";
			public static String CREATE = "<green>You've successfully created %arena_name% arena";
			public static String DELETE = "<green>You've successfully <red>deleted<green> the %arena_name% arena.";
			public static String SET_WAITING_LOCATION = "<green>You've successfully set the waiting spawn location for arena %arena_name%.";
			public static String SET_MAX_PLAYERS = "<green>You've successfully set the max players for arena %arena_name% to %arena_max_players%.";
			public static String SET_MIN_PLAYERS = "<green>You've successfully set the min players for arena %arena_name% to %arena_min_players%.";
			public static String ADD_SPAWN_LOCATION = "<green>You've successfully added a spawn location for arena %arena_name%.";
			public static String SET_MIN_Y = "<green>You've successfully set the min y for arena %arena_name% to %arena_min_y%.";
		}

		public static class Game {
			public static String NOT_IN_GAME = "<red>You're not in a game!";
			public static String JOIN = "<green>You've successfully joined the game!";
			public static String LEAVE = "<green>You've successfully <red>left <green>your game!";
			public static String FORCESTART = "<green>You've successfully <red>forcestarted <green>your game!";
			public static String FORCESTART_ALL = "<green>An administrator has <red>forcestarted <green>your game!";
			public static String WON = "<green>Congratulations you won!";
			public static String FORCESTOP_ALL = "<red>This game was forcestopped by an administrator!";
			public static String BOSSBAR_TITLE = "<green>Game starts in %seconds%";
		}

		public static class Stats {
			public static String MENU_TITLE = "%player_name%'s Stats";
		}


	}
}
