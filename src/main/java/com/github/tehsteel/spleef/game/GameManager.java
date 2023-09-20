package com.github.tehsteel.spleef.game;

import com.github.tehsteel.minigameapi.game.GameState;
import com.github.tehsteel.spleef.arena.model.SpleefArena;
import com.github.tehsteel.spleef.game.model.SpleefGame;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
public final class GameManager {

	private final Set<SpleefGame> games = new HashSet<>();

	public SpleefGame createGame(final SpleefArena arena) {
		final SpleefGame game = new SpleefGame(arena);

		games.add(game);

		return game;
	}

	public SpleefGame findFreeGame() {
		return games
				.stream()
				.filter(game -> game.getState() == GameState.WAITING)
				.findAny()
				.orElse(null);
	}

	public SpleefGame findGameByPlayer(final Player player) {
		return games
				.stream()
				.filter(game -> game.getPlayers().contains(player))
				.findFirst()
				.orElse(null);
	}
}
