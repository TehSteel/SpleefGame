package com.github.tehsteel.spleef.database;

import com.github.tehsteel.spleef.player.model.PlayerData;

import java.util.UUID;

public interface IDatabase {
	void connect();

	void createTables();

	void insertData(PlayerData data);

	PlayerData getPlayerDataByUuid(UUID uuid);

	boolean doesPlayerExist(UUID uuid);

	void disconnect();
}
