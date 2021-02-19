package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.Command;

public interface LinkExtractor {
	Optional<String> extract(String messageWithouCommand, final Command command);
}
