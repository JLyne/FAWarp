package fi.fabianadrian.fawarp.command;

import fi.fabianadrian.fawarp.FAWarp;
import io.papermc.paper.command.brigadier.Commands;

public abstract class FAWarpCommand {
	protected final FAWarp plugin;

	public FAWarpCommand(FAWarp plugin) {
		this.plugin = plugin;
	}

	public abstract void register(Commands registrar);
}
