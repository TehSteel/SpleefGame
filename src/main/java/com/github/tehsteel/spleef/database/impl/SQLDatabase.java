package com.github.tehsteel.spleef.database.impl;

import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.database.DataType;
import com.github.tehsteel.spleef.database.IDatabase;
import com.github.tehsteel.spleef.player.model.PlayerData;
import com.github.tehsteel.spleef.util.ConsoleUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class SQLDatabase implements IDatabase {
	private final SpleefPlugin plugin = SpleefPlugin.getInstance();

	@Getter private HikariDataSource hikari;

	@Override
	public void connect() {
		final HikariConfig config = new HikariConfig();


		if (plugin.getDatabaseManager().getDataType() == DataType.SQLITE) {
			final File file = new File(plugin.getDataFolder(), "database.db");
			config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath().replace("\\", "/"));

		} else {

			config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s",
					plugin.getSettingsConfig().getConfig().getString("DATABASE.Host"),
					plugin.getSettingsConfig().getConfig().getInt("DATABASE.Port"),
					plugin.getSettingsConfig().getConfig().getString("DATABASE.Database"))
			);
			config.setUsername(plugin.getSettingsConfig().getConfig().getString("DATABASE.Username"));
			config.setPassword(plugin.getSettingsConfig().getConfig().getString("DATABASE.Password"));
		}

		config.setMaximumPoolSize(10);
		config.setMinimumIdle(2);
		config.setConnectionTimeout(30000);

		config.setIdleTimeout(600000);
		config.setMaxLifetime(1800000);

		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		config.addDataSourceProperty("useServerPrepStmts", "true");

		config.setValidationTimeout(3000);

		config.setAutoCommit(true);

		hikari = new HikariDataSource(config);
		createTables();
	}

	@Override
	public void createTables() {
		final String query;
		if (plugin.getDatabaseManager().getDataType() == DataType.SQLITE) {
			query = """
					CREATE TABLE IF NOT EXISTS players (
					  id INTEGER PRIMARY KEY,
					  uuid TEXT NOT NULL,
					  name TEXT NOT NULL,
					  wins INTEGER DEFAULT NULL,
					  losses INTEGER DEFAULT NULL,
					  coins INTEGER DEFAULT NULL,
					  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
					);""";
		} else {
			query = """
					CREATE TABLE IF NOT EXISTS `players` (
					  `id` int(11) NOT NULL AUTO_INCREMENT,
					  `uuid` varchar(36) NOT NULL,
					  `name` varchar(16) NOT NULL,
					  `wins` int(11) DEFAULT NULL,
					  `losses` int(11) DEFAULT NULL,
					  `coins` int(11) DEFAULT NULL,
					  `last_updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
					  PRIMARY KEY (`id`),
					  UNIQUE KEY `uuid` (`uuid`),
					  KEY `name` (`name`),
					  KEY `last_updated` (`last_updated`)
					) ENGINE=InnoDB AUTO_INCREMENT=1;
					""";
		}
		try (PreparedStatement statement = hikari.getConnection().prepareStatement(query)) {


			statement.executeUpdate();


		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void insertData(final PlayerData data) {
		CompletableFuture.runAsync(() -> {
			try (PreparedStatement statement = hikari.getConnection().prepareStatement("INSERT INTO players (uuid, name, wins, losses, coins) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = ?, wins = ?, losses = ?, coins = ?")) {


				// Insert
				statement.setString(1, data.getUuid().toString());
				statement.setString(2, data.getName());
				statement.setInt(3, data.getWins());
				statement.setInt(4, data.getLosses());
				statement.setInt(5, data.getCoins());

				// Update Data
				statement.setString(6, data.getName());
				statement.setInt(7, data.getWins());
				statement.setInt(8, data.getLosses());
				statement.setInt(9, data.getCoins());

				statement.executeUpdate();


			} catch (final SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public CompletableFuture<PlayerData> getPlayerDataByUuid(final UUID uuid) {

		final PlayerData data;
		try (PreparedStatement statement = hikari.getConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?")) {

			statement.setString(1, uuid.toString());
			final ResultSet results = statement.executeQuery();

			if (results.next()) {
				data = new PlayerData(uuid, results.getString("name"));
				data.setWins(results.getInt("wins"));
				data.setLosses(results.getInt("losses"));
				data.setCoins(results.getInt("coins"));
			} else {
				data = null;
			}

			results.close();
		} catch (final SQLException e) {
			ConsoleUtil.log(e.getMessage());
			throw new RuntimeException(e);
		}

		return CompletableFuture.supplyAsync(() -> data);
	}

	@Override
	public boolean doesPlayerExist(final UUID uuid) {
		try (PreparedStatement statement = hikari.getConnection().prepareStatement("SELECT 1 FROM players WHERE uuid = ? LIMIT 1;")) {


			statement.setString(1, uuid.toString());
			final ResultSet results = statement.executeQuery();

			final boolean exists = results.next();


			results.close();

			return exists;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void disconnect() {
		if (hikari != null && !hikari.isClosed())
			hikari.close();
	}
}
