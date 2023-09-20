package com.github.tehsteel.spleef.player;

import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.player.model.PlayerData;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class PlayerManager {

	private final SpleefPlugin plugin = SpleefPlugin.getInstance();
	@Getter private ConcurrentMap<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();


	public PlayerData createPlayerData(final UUID uuid, final String name) {
		if (playerDataMap.get(uuid) != null)
			return playerDataMap.get(uuid);

		PlayerData data = plugin.getDatabaseManager().getDatabase().getPlayerDataByUuid(uuid);

		if (data == null) {
			final PlayerData playerData = new PlayerData(uuid, name);
			plugin.getDatabaseManager().getDatabase().insertData(playerData);
			data = playerData;
		}


		playerDataMap.put(uuid, data);

		return data;
	}

}

