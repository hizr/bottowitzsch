package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.MessageCommand;

public interface LinkExtractor {
	Optional<String> extract(String messageWithouCommand, final MessageCommand command);
}
