package fi.fabianadrian.fawarp.warp;

import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.config.ConfigurationManager;
import org.bukkit.Location;
import org.jspecify.annotations.Nullable;

import java.util.*;

public final class WarpManager {
	private final ConfigurationManager configurationManager;
	private final Map<String, Warp> warpMap = new HashMap<>();

	public WarpManager(FAWarp plugin) {
		this.configurationManager = plugin.configurationManager();
	}

	public void reload() {
		this.warpMap.clear();
		this.configurationManager.configuration().warps().forEach((name, location) -> this.warpMap.put(name, new Warp(name, location)));
	}

	public @Nullable Warp warp(String name) {
		return this.warpMap.get(name);
	}

	public List<Warp> warps() {
		return List.copyOf(this.warpMap.values());
	}

	public Location set(String name, Location location) {
		Warp warp = this.warpMap.put(name, new Warp(name, location));
		save();
		return warp == null ? null : warp.location();
	}

	public void unset(Warp warp) {
		this.warpMap.remove(warp.name());
		save();
	}

	private void save() {
		this.configurationManager.configuration().warps().clear();
		this.warpMap.values().forEach(warp -> this.configurationManager.configuration().warps().put(warp.name(), warp.location()));
		this.configurationManager.save();
	}
}
