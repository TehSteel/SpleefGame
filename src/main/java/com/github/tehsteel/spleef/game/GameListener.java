package com.github.tehsteel.spleef.game;

import com.github.tehsteel.minigameapi.api.game.GameCountdownEvent;
import com.github.tehsteel.minigameapi.api.game.GameEndEvent;
import com.github.tehsteel.minigameapi.api.game.GameStartEvent;
import com.github.tehsteel.minigameapi.api.game.player.GamePlayerJoinEvent;
import com.github.tehsteel.minigameapi.api.game.player.GamePlayerLoseEvent;
import com.github.tehsteel.minigameapi.api.game.player.GamePlayerQuitEvent;
import com.github.tehsteel.minigameapi.api.game.player.GamePlayerWinEvent;
import com.github.tehsteel.minigameapi.game.GameState;
import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.game.model.GamePlayer;
import com.github.tehsteel.spleef.game.model.SpleefGame;
import com.github.tehsteel.spleef.player.model.PlayerData;
import com.github.tehsteel.spleef.util.PlayerUtil;
import com.github.tehsteel.spleef.util.bossbar.CountdownBossBarBuilder;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class GameListener implements Listener {

	private final SpleefPlugin plugin = SpleefPlugin.getInstance();

	@EventHandler
	public void onGamePlayerJoinEvent(final GamePlayerJoinEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;
		final Player player = event.getPlayer();

		final GamePlayer gamePlayer = new GamePlayer(player.getUniqueId(), player);
		gamePlayer.setPlayerState(GamePlayerState.INLOBBY);

		game.getPlayerMap().put(player.getUniqueId(), gamePlayer);
		PlayerUtil.clear(player);
		player.teleport(game.getArena().getWaitingLocation());
		PlayerUtil.message(player, Constants.Messages.Game.JOIN);
	}

	@EventHandler
	public void onGamePlayerQuitEvent(final GamePlayerQuitEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;
		final Player player = event.getPlayer();

		game.getPlayerMap().remove(player.getUniqueId());
		PlayerUtil.message(player, Constants.Messages.Game.LEAVE);
		player.teleport(player.getWorld().getSpawnLocation());
		plugin.getPlayerManager().getPlayerDataMap().get(player.getUniqueId()).spawnToLobby();
		game.endGame(false);
	}

	@EventHandler
	public void onGamePlayerLoseEvent(final GamePlayerLoseEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;
		final Player player = event.getPlayer();
		final PlayerData data = plugin.getPlayerManager().getPlayerDataMap().get(player.getUniqueId());
		data.increaseLosses();

		game.endGame(false);
	}

	@EventHandler
	public void onGamePlayerWinEvent(final GamePlayerWinEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;
		final Player player = event.getPlayer();
		final PlayerData data = plugin.getPlayerManager().getPlayerDataMap().get(player.getUniqueId());
		PlayerUtil.message(player, Constants.Messages.Game.WON);
		data.increaseWins();

		final AtomicInteger time = new AtomicInteger(4);
		Bukkit.getScheduler().runTaskTimer(plugin, bukkitTask -> {
			final int timeLeft = time.getAndDecrement();


			player.spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 20);
			player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1F, 1F);

			if (timeLeft <= 0) {
				game.resetGame();
				bukkitTask.cancel();
			}
		}, 0, 20);
	}


	@EventHandler
	public void onGameStartEvent(final GameStartEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;

		game.getPlayerMap().values().forEach(gamePlayer -> {
			final Player player = gamePlayer.getPlayer();
			final List<Location> spawnLocations = game.getArena().getSpawnLocations().stream().toList();
			gamePlayer.setPlayerState(GamePlayerState.ALIVE);
			player.teleport(spawnLocations.get(new Random().nextInt(0, spawnLocations.size() - 1)));
			gamePlayer.giveItems();
		});

	}

	@EventHandler
	public void onGameCountdownEvent(final GameCountdownEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;

		if (event.isForceStart()) {
			game.getPlayers().forEach(player -> PlayerUtil.message(player, Constants.Messages.Game.FORCESTART_ALL));
		}


		CountdownBossBarBuilder
				.build(Constants.Messages.Game.BOSSBAR_TITLE.replace("%seconds%", String.valueOf(game.getCountdown())))
				.setGame(game)
				.setPlayers(game.getPlayers())
				.setOverlay(BossBar.Overlay.PROGRESS)
				.setBossBarTickCallback((seconds, players) -> players.forEach(player -> player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1F, 1F)))
				.setOnFinishCallback(() -> game.startGame(event.isForceStart()))
				.run();
	}


	@EventHandler
	public void onGameEndEvent(final GameEndEvent event) {
		if (!(event.getGame() instanceof final SpleefGame game)) return;

		if (event.isForceStopped()) {
			game.getPlayers().forEach(player -> PlayerUtil.message(player, Constants.Messages.Game.FORCESTOP_ALL));
			game.resetGame();
		} else {
			final GamePlayer winnerGamePlayer = game.getPlayerMap().values().stream().filter(gamePlayer -> gamePlayer.getPlayerState() == GamePlayerState.ALIVE).findFirst().orElse(null);
			if (winnerGamePlayer == null) {
				game.resetGame();
			} else {
				game.onPlayerWin(winnerGamePlayer.getPlayer());
			}
		}


	}

	@EventHandler
	public void onPlayerMoveEvent(final PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final SpleefGame game = plugin.getGameManager().findGameByPlayer(player);

		if (game == null) return;
		if (game.getState() != GameState.INGAME) return;

		if (event.getTo().getY() <= game.getArena().getMinY()) {
			game.onPlayerLose(player);
		}
	}
}
