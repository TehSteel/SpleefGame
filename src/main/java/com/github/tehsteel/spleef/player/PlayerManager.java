package com.github.tehsteel.spleef.player;

import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.player.model.PlayerData;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

public final class PlayerManager {

	private final SpleefPlugin plugin = SpleefPlugin.getInstance();
	@Getter private ConcurrentMap<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();


	public PlayerData createPlayerData(final UUID uuid, final String name) {
		if (playerDataMap.get(uuid) != null)
			return playerDataMap.get(uuid);

		final AtomicReference<PlayerData> data = new AtomicReference<>();
		plugin.getDatabaseManager().getDatabase().getPlayerDataByUuid(uuid).thenAcceptAsync(data::set);

		if (data.get() == null) {
			final PlayerData playerData = new PlayerData(uuid, name);
			data.set(playerData);
			plugin.getDatabaseManager().getDatabase().insertData(playerData);
		}


		playerDataMap.put(uuid, data.get());

		return data.get();
	}

}

