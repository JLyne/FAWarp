package fi.fabianadrian.fawarp.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.command.FAWarpCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;

import static io.papermc.paper.command.brigadier.Commands.literal;
import static net.kyori.adventure.text.Component.translatable;

public final class RootCommand extends FAWarpCommand {
	private static final Component COMPONENT_RELOAD = translatable("fawarp.command.root.reload");

	public RootCommand(FAWarp plugin) {
		super(plugin);
	}

	@Override
	public void register(Commands registrar) {
		LiteralCommandNode<CommandSourceStack> command = literal("fawarp")
				.requires(source -> source.getSender().hasPermission("fawarp.command.root.reload"))
				.then(literal("reload")
							.executes(this::reloadHandler)).build();

		registrar.register(command, "Base command");
	}

	private int reloadHandler(CommandContext<CommandSourceStack> ctx) {
		this.plugin.reload();
		ctx.getSource().getSender().sendMessage(COMPONENT_RELOAD);

		return Command.SINGLE_SUCCESS;
	}
}
