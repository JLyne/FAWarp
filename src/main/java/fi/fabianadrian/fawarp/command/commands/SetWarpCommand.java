package fi.fabianadrian.fawarp.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.command.FAWarpCommand;
import fi.fabianadrian.fawarp.util.ComponentUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.finePosition;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.world;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public final class SetWarpCommand extends FAWarpCommand {
	private static final TranslatableComponent COMPONENT_SETWARP = translatable("fawarp.command.setwarp");
	private static final TranslatableComponent COMPONENT_NOTPLAYER = translatable("fawarp.command.not_player");
	private static final TranslatableComponent COMPONENT_SETWARP_UPDATE = translatable("fawarp.command.setwarp.update");

	public SetWarpCommand(FAWarp plugin) {
		super(plugin);
	}

	@Override
	public void register(Commands registrar) {
		LiteralCommandNode<CommandSourceStack> command = literal("setwarp")
				.requires(source -> source.getSender().hasPermission("fawarp.command.setwarp"))
				.then(argument("name", word())
							.executes(this::setHandler)
							.then(argument("world", world())
										.then(argument("position", finePosition(true))
													.executes(this::setCoordinateHandler))))
				.build();

		registrar.register(command, "Creates a warp");
	}

	private int setHandler(CommandContext<CommandSourceStack> ctx) {
		CommandSender sender = ctx.getSource().getSender();

		if (!(sender instanceof Player player)) {
			sender.sendMessage(COMPONENT_NOTPLAYER);
			return Command.SINGLE_SUCCESS;
		}

		String warpName = ctx.getArgument("name", String.class);

		Location previousLocation = this.plugin.warpManager().set(warpName, player.getLocation());

		if (previousLocation != null) {
			sender.sendMessage(COMPONENT_SETWARP_UPDATE.arguments(text(warpName), ComponentUtils.locationComponent(previousLocation), ComponentUtils.locationComponent(player.getLocation())));
		} else {
			sender.sendMessage(COMPONENT_SETWARP.arguments(text(warpName), ComponentUtils.locationComponent(player.getLocation())));
		}

		return Command.SINGLE_SUCCESS;
	}

	@SuppressWarnings("UnstableApiUsage")
	private int setCoordinateHandler(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSender sender = ctx.getSource().getSender();
		FinePosition positon = ctx.getArgument("position", FinePositionResolver.class).resolve(ctx.getSource());
		World world = ctx.getArgument("world", World.class);

		Location location = new Location(world, positon.x(), positon.y(), positon.z());
		String warpName = ctx.getArgument("name", String.class);

		Location previousLocation = this.plugin.warpManager().set(warpName, location);
		if (sender instanceof Player player) {
			player.teleport(location);
		}

		if (previousLocation != null) {
			sender.sendMessage(COMPONENT_SETWARP_UPDATE.arguments(text(warpName), ComponentUtils.locationComponent(previousLocation), ComponentUtils.locationComponent(location)));
		} else {
			sender.sendMessage(COMPONENT_SETWARP.arguments(text(warpName), ComponentUtils.locationComponent(location)));
		}

		return Command.SINGLE_SUCCESS;
	}
}
