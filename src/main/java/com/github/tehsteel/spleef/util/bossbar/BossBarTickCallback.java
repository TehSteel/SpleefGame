package com.github.tehsteel.spleef.util.bossbar;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface BossBarTickCallback {
	void onTick(int seconds, Collection<Player> players);
}
