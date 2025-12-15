package fi.fabianadrian.fawarp.locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public final class TranslationManager {
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	public static final List<Locale> BUNDLED_LOCALES = List.of(Locale.of("fi", "FI"));

	private final Logger logger;
	private final TranslationStore.@NotNull StringBased<MessageFormat> registry;

	public TranslationManager(Logger logger) {
		this.logger = logger;

		this.registry = TranslationStore.messageFormat(Key.key("faspawn", "main"));
		this.registry.defaultLocale(DEFAULT_LOCALE);

		loadFromResourceBundle();

		// register it to the global source, so our translations can be picked up by adventure-platform
		GlobalTranslator.translator().addSource(this.registry);
	}

	/**
	 * Loads the bundled translations from the jar file.
	 */
	private void loadFromResourceBundle() {
		ResourceBundle defaultBundle = ResourceBundle.getBundle("messages", DEFAULT_LOCALE);
		try {
			this.registry.registerAll(DEFAULT_LOCALE, defaultBundle, false);
			BUNDLED_LOCALES.forEach(locale -> {
				ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
				this.registry.registerAll(locale, bundle, false);
			});
		} catch (IllegalArgumentException e) {
			this.logger.warn("Error loading default locale file", e);
		}
	}
}
