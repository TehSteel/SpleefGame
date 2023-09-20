package com.github.tehsteel.spleef.game.model;

import com.github.tehsteel.minigameapi.arena.ArenaState;
import com.github.tehsteel.minigameapi.game.model.Game;
import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.arena.model.SpleefArena;
import com.github.tehsteel.spleef.game.GamePlayerState;
import com.github.tehsteel.spleef.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.block.Block;

import java.util.*;

@Getter
public final class SpleefGame extends Game {

	private final Map<UUID, GamePlayer> playerMap = new HashMap<>();
	private final List<SavedBlock> brokenBlocks = new ArrayList<>();

	public SpleefGame(final SpleefArena arena) {
		super(arena);
	}

	@Override
	public void resetGame() {

		getPlayers().forEach(player -> {
			PlayerUtil.clear(player);
			player.teleport(Constants.Settings.SPAWN_LOCATION);
			SpleefPlugin.getInstance().getPlayerManager().getPlayerDataMap().get(player.getUniqueId()).spawnToLobby();
		});

		brokenBlocks.forEach(block -> {
			final Block toReplaceBlock = block.location().getWorld().getBlockAt(block.location());
			toReplaceBlock.setType(block.material());
			toReplaceBlock.setBlockData(block.blockData());
		});

		getArena().setState(ArenaState.READY);

		getPlayerMap().clear();
		getPlayers().clear();
		brokenBlocks.clear();
		SpleefPlugin.getInstance().getGameManager().getGames().remove(this);
	}

	@Override
	public boolean shouldGameEnd() {
		return playerMap.values().stream()
				.filter(gamePlayer -> gamePlayer.getPlayerState() == GamePlayerState.ALIVE)
				.count() <= 1;
	}

	@Override
	public SpleefArena getArena() {
		return (SpleefArena) super.getArena();
	}
}
