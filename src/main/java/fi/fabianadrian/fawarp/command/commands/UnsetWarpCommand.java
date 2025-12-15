package fi.fabianadrian.fawarp.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.command.FAWarpCommand;
import fi.fabianadrian.fawarp.command.parser.WarpArgumentType;
import fi.fabianadrian.fawarp.warp.Warp;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.TranslatableComponent;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public final class UnsetWarpCommand extends FAWarpCommand {
	private static final TranslatableComponent COMPONENT_UNSETWARP = translatable("fawarp.command.unsetwarp");
	private final WarpArgumentType warpArgumentType;

	public UnsetWarpCommand(FAWarp plugin) {
		warpArgumentType = new WarpArgumentType(plugin);
		super(plugin);
	}

	@Override
	public void register(Commands registrar) {
		LiteralCommandNode<CommandSourceStack> command = literal("unsetwarp")
				.requires(source -> source.getSender().hasPermission("fawarp.command.unsetwarp"))
				.then(argument("warp", warpArgumentType)
							.executes(this::unsetHandler))
				.build();

		registrar.register(command, "Deletes a warp");
	}

	private int unsetHandler(CommandContext<CommandSourceStack> ctx) {
		Warp warp = ctx.getArgument("warp", Warp.class);
		this.plugin.warpManager().unset(warp);
		ctx.getSource().getSender().sendMessage(COMPONENT_UNSETWARP.arguments(text(warp.name())));

		return Command.SINGLE_SUCCESS;
	}
}
