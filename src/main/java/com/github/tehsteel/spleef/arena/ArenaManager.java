package com.github.tehsteel.spleef.arena;

import com.github.tehsteel.minigameapi.arena.ArenaException;
import com.github.tehsteel.minigameapi.arena.model.Arena;
import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.arena.model.SpleefArena;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ArenaManager {

	private final SpleefPlugin plugin = SpleefPlugin.getInstance();

	@Getter
	private Map<String, SpleefArena> arenas = new HashMap<>();


	public ArenaManager() {
		loadArenas();
	}


	public SpleefArena createArena(final SpleefArena arena) {
		return createArena(arena.getName(), arena.getWaitingLocation(), arena.getMaxPlayers(), arena.getMinPlayers());
	}

	public SpleefArena createArena(final String name) {
		return createArena(name, null, Constants.ArenaSettings.MAX_PLAYERS, Constants.ArenaSettings.MIN_PLAYERS);
	}

	public SpleefArena createArena(@NonNull final String name, final Location spawnLocation, final int maxPlayers, final int minPlayers) {
		final SpleefArena arena = new SpleefArena(name, spawnLocation, maxPlayers, minPlayers);

		arenas.put(name, arena);


		try {
			arena.saveConfig();
		} catch (final ArenaException e) {
			throw new RuntimeException(e);
		}

		return arena;
	}

	public void deleteArena(final Arena arena) {
		arena.deleteArenaConfig();
	}

	public void deleteArena(final String name) {
		deleteArena(findArenaByName(name));
	}

	public SpleefArena findArenaByName(@NonNull final String name) {
		return arenas.get(name);
	}


	public SpleefArena getFreeArena() {
		return arenas
				.values()
				.stream()
				.filter(Arena::isArenaReady)
				.findFirst().orElse(null);
	}

	private void loadArenas() {
		if (!new File(plugin.getDataFolder() + "/arenas").exists()) return;

		Arrays.stream(Objects.requireNonNull(new File(plugin.getDataFolder() + "/arenas").listFiles())).forEach(file -> {
			final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

			if (config.getString("ArenaData.name") != null) {
				final SpleefArena arena = SpleefArena.deserialize(config);

				arenas.put(arena.getName(), arena);
			}
		});
	}
}
