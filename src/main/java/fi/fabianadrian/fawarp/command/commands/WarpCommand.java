package fi.fabianadrian.fawarp.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.command.FAWarpCommand;
import fi.fabianadrian.fawarp.command.parser.WarpArgumentType;
import fi.fabianadrian.fawarp.warp.Warp;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.players;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public final class WarpCommand extends FAWarpCommand {
	private static final TranslatableComponent COMPONENT_NOTPLAYER = translatable("fawarp.command.not_player");
	private static final TranslatableComponent COMPONENT_WARP = translatable("fawarp.command.warp");
	private static final TranslatableComponent COMPONENT_MULTIPLE = translatable("fawarp.command.warp.player.multiple");
	private static final TranslatableComponent COMPONENT_SINGLE = translatable("fawarp.command.warp.player.single");

	private final WarpArgumentType warpArgumentType;

	public WarpCommand(FAWarp plugin) {
		super(plugin);
		warpArgumentType = new WarpArgumentType(plugin);
	}

	@Override
	public void register(Commands registrar) {
		LiteralCommandNode<CommandSourceStack> command = literal("warp")
				.then(argument("warp", warpArgumentType)
							.requires(source -> source.getSender().hasPermission("fawarp.command.warp"))
							.executes(this::warpHandler)
							.then(argument("player", players())
										.requires(source -> source.getSender().hasPermission("fawarp.command.warp.player"))
										.executes(this::warpOtherHandler)))
				.build();

		registrar.register(command, "Performs a warp");
	}

	private int warpHandler(CommandContext<CommandSourceStack> ctx) {
		CommandSender sender = ctx.getSource().getSender();
		Entity executor = ctx.getSource().getExecutor();

		if (!(sender instanceof Player) && executor != sender) {
			sender.sendMessage(COMPONENT_NOTPLAYER);
			return Command.SINGLE_SUCCESS;
		}

		if (executor != null) {
			Warp warp = ctx.getArgument("warp", Warp.class);
			executor.teleport(warp.location());
			executor.sendMessage(COMPONENT_WARP.arguments(warp.nameAsComponent()));
		}

		return Command.SINGLE_SUCCESS;
	}

	private int warpOtherHandler(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		Warp warp = ctx.getArgument("warp", Warp.class);
		List<Player> players = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());

		players.forEach(player -> {
			player.teleport(warp.location());
			player.sendMessage(COMPONENT_WARP.arguments(warp.nameAsComponent()));
		});

		Component message;
		if (players.size() > 1) {
			message = COMPONENT_MULTIPLE.arguments(text(players.size()), warp.nameAsComponent());
		} else {
			Entity entity = players.getFirst();
			message = COMPONENT_SINGLE.arguments(entity.name(), warp.nameAsComponent());
		}

		ctx.getSource().getSender().sendMessage(message);
		return Command.SINGLE_SUCCESS;
	}
}
