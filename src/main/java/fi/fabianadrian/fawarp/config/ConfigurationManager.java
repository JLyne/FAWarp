package fi.fabianadrian.fawarp.config;

import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.config.serializer.LocationSerializer;
import org.bukkit.Location;
import org.spongepowered.configurate.ConfigurateException;

public final class ConfigurationManager {
	private final ConfigurationLoader<Configuration> configurationLoader;
	private Configuration configuration;

	public ConfigurationManager(FAWarp plugin) {
		this.configurationLoader = new ConfigurationLoader<>(Configuration.class, plugin.getServer().getPluginsFolder().toPath().resolve("FabianAdrian/warp.yml"), options -> options.serializers(build -> build.register(Location.class, LocationSerializer.INSTANCE)));
	}

	public void reload() {
		try {
			this.configuration = this.configurationLoader.load();
		} catch (ConfigurateException e) {
			throw new IllegalStateException("Failed to load config", e);
		}
	}

	public Configuration configuration() {
		if (this.configuration == null) {
			throw new IllegalStateException("Config has not yet been loaded");
		}
		return this.configuration;
	}

	public void save() {
		try {
			this.configurationLoader.save(this.configuration);
		} catch (ConfigurateException e) {
			throw new IllegalStateException("Failed to save config", e);
		}
	}
}
