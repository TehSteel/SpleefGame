package com.github.tehsteel.spleef.game.model;

import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.game.GamePlayerState;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public final class GamePlayer {

	private final UUID uuid;
	private final Player player;
	private GamePlayerState playerState;

	public void giveItems() {
		player.getInventory().setItem(Constants.Settings.SHOVEL_ITEM.slot(), Constants.Settings.SHOVEL_ITEM.item());
	}
}
