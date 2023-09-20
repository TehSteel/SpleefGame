package com.github.tehsteel.spleef.runnable;

import com.github.tehsteel.spleef.SpleefPlugin;
import com.github.tehsteel.spleef.player.model.PlayerData;

public final class SaveDataRunnable implements Runnable {
	@Override
	public void run() {
		for (final PlayerData playerData : SpleefPlugin.getInstance().getPlayerManager().getPlayerDataMap().values())
			SpleefPlugin.getInstance().getDatabaseManager().getDatabase().insertData(playerData);

	}
}
