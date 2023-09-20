package com.github.tehsteel.spleef.listener;

import com.github.tehsteel.minigameapi.game.GameState;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.game.GamePlayerState;
import com.github.tehsteel.spleef.game.model.GamePlayer;
import com.github.tehsteel.spleef.game.model.SavedBlock;
import com.github.tehsteel.spleef.game.model.SpleefGame;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public final class WorldListener implements Listener {

	private final SpleefPlugin plugin = SpleefPlugin.getInstance();

	@EventHandler
	public void onBlockBreakEvent(final BlockBreakEvent event) {
		final Player player = event.getPlayer();
		final SpleefGame game = plugin.getGameManager().findGameByPlayer(player);
		final Block block = event.getBlock();

		if (game == null && player.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
		assert game != null;
		if (game.getState() != GameState.INGAME) {
			event.setCancelled(true);
			return;
		}
		final GamePlayer gamePlayer = game.getPlayerMap().get(player.getUniqueId());
		if (gamePlayer == null) return;

		if (gamePlayer.getPlayerState() == GamePlayerState.SPECTATOR)
			event.setCancelled(true);


		game.getBrokenBlocks().add(new SavedBlock(block.getType(), block.getBlockData(), block.getLocation()));
	}

	@EventHandler
	public void onBlockPlaceEvent(final BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		final SpleefGame game = plugin.getGameManager().findGameByPlayer(player);
		if (game == null && player.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
		assert game != null;
		if (game.getState() != GameState.INGAME) {
			event.setCancelled(true);
			return;
		}
		final GamePlayer gamePlayer = game.getPlayerMap().get(player.getUniqueId());
		if (gamePlayer == null) return;

		if (gamePlayer.getPlayerState() == GamePlayerState.SPECTATOR)
			event.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChangeEvent(final FoodLevelChangeEvent event) {
		event.getEntity().setFoodLevel(20);
		event.setCancelled(true);
	}
}
