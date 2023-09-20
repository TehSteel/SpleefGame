package com.github.tehsteel.spleef.util;

import com.github.tehsteel.spleef.Constants;
import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

/**
 * Utility class for console logging etc.
 *
 * @author TehSteel
 */
public final class ConsoleUtil {

	private ConsoleUtil() {
	}

	public static void log(final String message, final Object... replace) {
		log(String.format(message, replace));
	}

	public static void log(@NonNull final String message) {
		Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("[" + Constants.PLUGIN_NAME + "] " + message));
	}
}
