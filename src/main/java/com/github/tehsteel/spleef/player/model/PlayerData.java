package com.github.tehsteel.spleef.player.model;

import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.util.PlayerUtil;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public final class PlayerData {

	private final UUID uuid;
	private final String name;
	private Player player;
	private int wins = 0;
	private int losses = 0;
	private int coins = 0;

	public void increaseWins() {
		wins++;
	}


	public void increaseLosses() {
		losses++;
	}

	public void increaseCoins(final int coins) {
		this.coins += coins;
	}


	public void spawnToLobby() {
		PlayerUtil.clear(player);

		player.getInventory().setItem(Constants.Settings.COMPASS_ITEM.slot(), Constants.Settings.COMPASS_ITEM.item());
	}
}
