package com.github.tehsteel.spleef.util;

import com.github.tehsteel.spleef.SpleefPlugin;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author TehSteel
 */
@Getter
public final class CustomConfig {

	private final File configFile;
	private FileConfiguration config;

	public CustomConfig(final String name) throws IOException, InvalidConfigurationException {
		configFile = new File(SpleefPlugin.getInstance().getDataFolder(), name + ".yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			SpleefPlugin.getInstance().saveResource(name + ".yml", false);
		}

		config = new YamlConfiguration();
		config.load(configFile);
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public void saveConfig() throws IOException {
		config.save(configFile);
		reloadConfig();
	}
}
