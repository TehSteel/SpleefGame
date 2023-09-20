package com.github.tehsteel.spleef.arena.model;

import com.github.tehsteel.minigameapi.arena.ArenaException;
import com.github.tehsteel.minigameapi.arena.model.Arena;
import com.github.tehsteel.minigameapi.util.CustomLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

@Getter
public class SpleefArena extends Arena {


	@Setter private double minY;

	public SpleefArena(final String name) {
		super(name);
	}

	public SpleefArena(final Arena arena) {
		super(arena);
	}

	public SpleefArena(final String name, final Location waitingLocation, final int maxPlayers, final int minPlayers) {
		super(name, waitingLocation, maxPlayers, minPlayers);
	}

	public static SpleefArena deserialize(final FileConfiguration config) {
		final SpleefArena arena = new SpleefArena(config.getString("ArenaData.name"));

		if (config.getString("ArenaData.waitingLocation") != null)
			arena.setWaitingLocation(CustomLocation.deserialize(Objects.requireNonNull(config.getString("ArenaData.waitingLocation"))).toBukkitLocation());

		arena.setMaxPlayers(config.getInt("ArenaData.maxPlayers"));
		arena.setMinPlayers(config.getInt("ArenaData.minPlayers"));


		arena.setMinY(config.getDouble("ArenaData.minY"));

		final ConfigurationSection section = config.getConfigurationSection("ArenaData.spawnLocations");

		if (section != null) {
			for (int i = 0; i < section.getKeys(false).size(); i++) {
				arena.getSpawnLocations().add(CustomLocation.deserialize(Objects.requireNonNull(section.getString(String.valueOf(i)))).toBukkitLocation());
			}
		}

		return arena;
	}

	@Override
	public void serialize() throws ArenaException {
		getConfig().set("ArenaData.name", getName());
		if (getWaitingLocation() != null) {
			getConfig().set("ArenaData.waitingLocation", CustomLocation.fromBukkitLocation(getWaitingLocation()).serialize());
		}

		getConfig().set("ArenaData.maxPlayers", getMaxPlayers());
		getConfig().set("ArenaData.minPlayers", getMinPlayers());

		if (getSpawnLocations() != null && !getSpawnLocations().isEmpty()) {
			for (int i = 0; i < getSpawnLocations().size(); ++i) {
				getConfig().set("ArenaData.spawnLocations." + i, CustomLocation.fromBukkitLocation(getSpawnLocations().stream().toList().get(i)).serialize());
			}
		}

		getConfig().set("ArenaData.minY", minY);

		saveConfig();
	}
}
