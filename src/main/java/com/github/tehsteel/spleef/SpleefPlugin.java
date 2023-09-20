package com.github.tehsteel.spleef;

import com.github.tehsteel.minigameapi.MiniGameLib;
import com.github.tehsteel.spleef.arena.ArenaManager;
import com.github.tehsteel.spleef.command.ArenaCommand;
import com.github.tehsteel.spleef.command.GameCommand;
import com.github.tehsteel.spleef.command.SpleefGameCommand;
import com.github.tehsteel.spleef.command.StatsCommand;
import com.github.tehsteel.spleef.database.DataType;
import com.github.tehsteel.spleef.database.DatabaseManager;
import com.github.tehsteel.spleef.game.GameListener;
import com.github.tehsteel.spleef.game.GameManager;
import com.github.tehsteel.spleef.listener.WorldListener;
import com.github.tehsteel.spleef.player.PlayerListener;
import com.github.tehsteel.spleef.player.PlayerManager;
import com.github.tehsteel.spleef.runnable.SaveDataRunnable;
import com.github.tehsteel.spleef.util.ConsoleUtil;
import com.github.tehsteel.spleef.util.CustomConfig;
import com.github.tehsteel.spleef.util.menu.MenuListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public final class SpleefPlugin extends JavaPlugin {
	@Getter private static SpleefPlugin instance;


	@Getter private CustomConfig settingsConfig, messagesConfig;

	@Getter private DatabaseManager databaseManager;
	@Getter private PlayerManager playerManager;
	@Getter private GameManager gameManager;
	@Getter private ArenaManager arenaManager;


	@Override
	public void onEnable() {
		instance = this;

		registerFiles();
		registerManagers();
		registerCommands();
		registerListeners();
	}


	@Override
	public void onDisable() {
		instance = null;
	}


	private void registerFiles() {
		ConsoleUtil.log("<gray>Registering files...<gray>");
		final long startTime = System.currentTimeMillis();
		try {
			settingsConfig = new CustomConfig("settings");
			messagesConfig = new CustomConfig("messages");
			Constants.loadConstants();
		} catch (IOException | InvalidConfigurationException e) {
			ConsoleUtil.log("<red>Failed to register files.</red>");
			throw new RuntimeException(e);
		}

		ConsoleUtil.log("<green>Registered files successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}

	private void registerManagers() {
		ConsoleUtil.log("Registering managers...");
		final long startTime = System.currentTimeMillis();

		MiniGameLib.setPlugin(this);


		databaseManager = new DatabaseManager(DataType.fromString(Objects.requireNonNull(settingsConfig.getConfig().getString("Database.DataType"))));
		databaseManager.getDatabase().connect();
		playerManager = new PlayerManager();
		arenaManager = new ArenaManager();
		gameManager = new GameManager();

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new SaveDataRunnable(), 3600, 3600);

		ConsoleUtil.log("<green>Registered managers successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}

	private void registerCommands() {
		ConsoleUtil.log("Registering commands...");
		final long startTime = System.currentTimeMillis();


		Arrays.asList(
				new ArenaCommand(),
				new StatsCommand(),
				new GameCommand(),
				new SpleefGameCommand()
		).forEach(this::registerCommand);

		ConsoleUtil.log("<green>Registered commands successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}


	private void registerListeners() {
		ConsoleUtil.log("Registering events...");
		final long startTime = System.currentTimeMillis();
		Arrays.asList(
				new GameListener(),
				new WorldListener(),
				new PlayerListener(),
				new MenuListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		ConsoleUtil.log("<green>Registered listeners successfully in %s ms.</green>", (System.currentTimeMillis() - startTime));
	}

	private void registerCommand(final Command command) {
		try {
			final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);

			final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());
			commandMap.register(command.getLabel(), command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
