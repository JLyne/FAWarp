package fi.fabianadrian.fawarp;

import fi.fabianadrian.fawarp.command.commands.*;
import fi.fabianadrian.fawarp.config.ConfigurationManager;
import fi.fabianadrian.fawarp.listener.ServerListener;
import fi.fabianadrian.fawarp.locale.TranslationManager;
import fi.fabianadrian.fawarp.warp.WarpManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class FAWarp extends JavaPlugin {
	private ConfigurationManager configurationManager;
	private WarpManager warpManager;

	@Override
	public void onEnable() {
		new TranslationManager(getSLF4JLogger());

		this.configurationManager = new ConfigurationManager(this);
		this.warpManager = new WarpManager(this);

		setupCommandManager();
		registerListeners();
	}

	public void reload() {
		this.configurationManager.reload();
		this.warpManager.reload();
	}

	public ConfigurationManager configurationManager() {
		return this.configurationManager;
	}

	public WarpManager warpManager() {
		return this.warpManager;
	}

	private void setupCommandManager() {
		getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, e -> {
			registerCommands(e.registrar());
		});
	}

	private void registerCommands(Commands registrar) {
		List.of(
				new RootCommand(this),
				new SetWarpCommand(this),
				new UnsetWarpCommand(this),
				new WarpCommand(this),
				new WarpListCommand(this)
		).forEach(c -> c.register(registrar));
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		List.of(new ServerListener(this)).forEach(listener -> manager.registerEvents(listener, this));
	}
}
