package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.MessageCommand;
import org.springframework.stereotype.Component;

@Component
public class SimpleSplitFromExapleLinkExtractor implements LinkExtractor {

	@Override
	public Optional<String> extract(final String message, final MessageCommand command) {
		final String value = message.split(" ")[1];
		return Optional.ofNullable(value);
	}
}
