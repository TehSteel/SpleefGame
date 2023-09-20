package com.github.tehsteel.spleef.util.bossbar;

import com.github.tehsteel.spleef.Constants;
import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.game.model.SpleefGame;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

@Getter
public final class CountdownBossBarBuilder {

	private final String title;
	private BossBar.Color color;
	private BossBar.Overlay overlay;
	private Collection<BossBar.Flag> flags;
	private int seconds = 10;
	private Collection<Player> players;
	private BossBarCallback onFinishCallback;
	private BossBarTickCallback bossBarTickCallback;
	private SpleefGame game;


	private CountdownBossBarBuilder(final String title) {
		this.title = title;
	}

	public static CountdownBossBarBuilder build(final String title) {
		return new CountdownBossBarBuilder(title);
	}

	public CountdownBossBarBuilder setColor(final BossBar.Color color) {
		this.color = color;

		return this;
	}

	public CountdownBossBarBuilder setOverlay(final BossBar.Overlay overlay) {
		this.overlay = overlay;

		return this;
	}

	public CountdownBossBarBuilder setFlags(final Collection<BossBar.Flag> flags) {
		this.flags = flags;

		return this;
	}

	public CountdownBossBarBuilder setSeconds(final int seconds) {
		this.seconds = seconds;

		return this;
	}

	public CountdownBossBarBuilder setPlayers(final Collection<Player> players) {
		this.players = players;

		return this;
	}

	public CountdownBossBarBuilder setOnFinishCallback(final BossBarCallback onFinishCallback) {
		this.onFinishCallback = onFinishCallback;

		return this;
	}

	public CountdownBossBarBuilder setBossBarTickCallback(final BossBarTickCallback bossBarTickCallback) {
		this.bossBarTickCallback = bossBarTickCallback;

		return this;
	}

	public CountdownBossBarBuilder setGame(final SpleefGame game) {
		this.game = game;

		return this;
	}

	public void run() {
		final BossBar bossBar = BossBar.bossBar(MiniMessage.miniMessage().deserialize(title), 0, BossBar.Color.RED, BossBar.Overlay.PROGRESS);


		if (flags != null && !flags.isEmpty())
			bossBar.addFlags(flags);

		players.forEach(bossBar::addViewer);


		if (color != null)
			bossBar.color(color);


		if (overlay != null)
			bossBar.overlay(overlay);


		Bukkit.getScheduler().runTaskTimer(SpleefPlugin.getInstance(), bukkitTask -> {
			players = game.getPlayers();
			seconds--;

			players.forEach(bossBar::addViewer);

			if (bossBarTickCallback != null)
				bossBarTickCallback.onTick(seconds, players);


			bossBar.name(MiniMessage.miniMessage().deserialize(Constants.Messages.Game.BOSSBAR_TITLE.replace("%seconds%", String.valueOf(this.seconds))));

			if (game != null)
				bossBar.progress((float) seconds / game.getCountdown());

			if (seconds < 1) {
				bossBar.viewers().forEach(bossBarViewer -> bossBar.removeViewer((Audience) bossBarViewer));
				if (onFinishCallback != null)
					onFinishCallback.onFinish();
				bukkitTask.cancel();
			}

		}, 20, 20);
	}
}
