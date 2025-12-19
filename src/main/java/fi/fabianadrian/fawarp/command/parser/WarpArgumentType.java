package fi.fabianadrian.fawarp.command.parser;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fi.fabianadrian.fawarp.FAWarp;
import fi.fabianadrian.fawarp.warp.Warp;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class WarpArgumentType implements CustomArgumentType<Warp, String> {
	private static final SimpleCommandExceptionType PARSE_FAILED = new SimpleCommandExceptionType(
		MessageComponentSerializer.message().serialize(Component.translatable("argument.parse.failure.warp")));

	private final FAWarp plugin;

	public WarpArgumentType(FAWarp plugin) {
		this.plugin = plugin;
	}

	@Override
	public Warp parse(@NonNull StringReader reader) throws CommandSyntaxException {
		String string = getNativeType().parse(reader);
		Warp warp = plugin.warpManager().warp(string);

		if (warp == null) {
			throw PARSE_FAILED.create();
		}

		return warp;
	}

	@Override
	public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
			CommandContext<S> ctx, @NonNull SuggestionsBuilder builder) {
		if (!(ctx.getSource() instanceof CommandSourceStack stack)) {
			return builder.buildFuture();
		}

		List<Warp> warps = plugin.warpManager().warps();
		warps.stream().filter(warp -> stack.getSender().hasPermission(warp.permission()))
				.map(Warp::name)
				.filter(n -> n.toLowerCase().startsWith(builder.getRemainingLowerCase()))
				.forEach(builder::suggest);

		return builder.buildFuture();
	}

	@Override
	public @NonNull ArgumentType<String> getNativeType() {
		return StringArgumentType.word();
	}
}
