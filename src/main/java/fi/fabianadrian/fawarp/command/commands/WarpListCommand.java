package fi.fabianadrian.fawarp.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.command.FAWarpCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.List;
import java.util.StringJoiner;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public final class WarpListCommand extends FAWarpCommand {
	private static final Component COMPONENT_HEADER = translatable("fawarp.command.warplist.header");
	private static final Component COMPONENT_EMPTY = translatable("fawarp.command.warplist.empty");

	public WarpListCommand(FAWarp plugin) {
		super(plugin);
	}

	@Override
	public void register(Commands registar) {
		LiteralCommandNode<CommandSourceStack> command = Commands.literal("warplist")
				.requires(source -> source.getSender().hasPermission("fawarp.command.warplist"))
				.executes(this::listHandler)
				.build();

		registar.register(command, "Lists available warps", List.of("warps"));
	}

	private int listHandler(CommandContext<CommandSourceStack> ctx) {
		StringJoiner joiner = new StringJoiner(", ");
		this.plugin.warpManager().warps().forEach(warp -> {
			if (!ctx.getSource().getSender().hasPermission(warp.permission())) {
				return;
			}

			joiner.add(warp.name());
		});

		String availableWarps = joiner.toString();
		if (availableWarps.isBlank()) {
			ctx.getSource().getSender().sendMessage(COMPONENT_EMPTY);
			return Command.SINGLE_SUCCESS;
		}

		ctx.getSource().getSender().sendMessage(Component.join(JoinConfiguration.newlines(), COMPONENT_HEADER, text(joiner.toString())));

		return Command.SINGLE_SUCCESS;
	}
}
