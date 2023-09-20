package com.github.tehsteel.spleef.player;

import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.arena.model.SpleefArena;
import com.github.tehsteel.spleef.game.model.SpleefGame;
import com.github.tehsteel.spleef.player.model.PlayerData;
import com.github.tehsteel.spleef.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerListener implements Listener {

	private final SpleefPlugin plugin = SpleefPlugin.getInstance();

	@EventHandler
	public void onAsyncPlayerPreLoginEvent(final AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
		plugin.getPlayerManager().createPlayerData(event.getUniqueId(), event.getName());
	}

	@EventHandler
	public void onPlayerJoinEvent(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PlayerData playerData = plugin.getPlayerManager().getPlayerDataMap().get(player.getUniqueId());

		if (playerData == null) {
			player.kick();
			return;
		}

		playerData.setPlayer(player);
		playerData.spawnToLobby();
	}

	@EventHandler
	public void onPlayerInteractEvent(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().isSimilar(Constants.Settings.COMPASS_ITEM.item())) {
			SpleefGame game = plugin.getGameManager().findFreeGame();

			if (game == null) {
				final SpleefArena arena = SpleefPlugin.getInstance().getArenaManager().getFreeArena();

				if (arena == null) {
					PlayerUtil.message(player, "<red>No free arena for a game was available!");
					return;
				}

				game = plugin.getGameManager().createGame(arena);
				game.addPlayer(player);
			}

		}
	}
}
