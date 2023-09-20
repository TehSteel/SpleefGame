package com.github.tehsteel.spleef.database;

import com.github.tehsteel.spleef.player.model.PlayerData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IDatabase {
	void connect();

	void createTables();

	void insertData(PlayerData data);

	CompletableFuture<PlayerData> getPlayerDataByUuid(UUID uuid);

	boolean doesPlayerExist(UUID uuid);

	void disconnect();
}
