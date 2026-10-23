package fi.fabianadrian.fawarp.config.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;

public final class LocationSerializer implements TypeSerializer<Location> {
	public static final LocationSerializer INSTANCE = new LocationSerializer();

	private static final String WORLD = "world";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String PITCH = "pitch";
	private static final String YAW = "yaw";

	@Override
	public Location deserialize(Type type, ConfigurationNode source) throws SerializationException {
		final ConfigurationNode worldNode = nonVirtualNode(source, WORLD);
		final String worldName = worldNode.getString();
		World world = null;

		if (worldName == null) {
			throw new SerializationException("World name was null!");
		}

		NamespacedKey worldKey = NamespacedKey.fromString(worldName);

		if(worldKey != null) {
			world = Bukkit.getWorld(worldKey);
		}

		// Fallback to world name
		if (world == null) {
			//noinspection UsagesOfObsoleteApi
			world = Bukkit.getWorld(worldName);
		}

		if (world == null) {
			throw new SerializationException("World does not exist or is not loaded yet!");
		}

		return new Location(world, nonVirtualNode(source, X).getDouble(), nonVirtualNode(source, Y).getDouble(), nonVirtualNode(source, Z).getDouble(), nonVirtualNode(source, YAW).getFloat(), nonVirtualNode(source, PITCH).getFloat());
	}

	@Override
	public void serialize(Type type, @Nullable Location location, ConfigurationNode target) throws SerializationException {
		if (location == null) {
			target.raw(null);
			return;
		}

		target.node(WORLD).set(location.getWorld().getKey());
		target.node(X).set(location.getX());
		target.node(Y).set(location.getY());
		target.node(Z).set(location.getZ());
		target.node(YAW).set(location.getYaw());
		target.node(PITCH).set(location.getPitch());
	}

	private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path) throws SerializationException {
		if (!source.hasChild(path)) {
			throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
		}
		return source.node(path);
	}
}
