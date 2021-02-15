package de.hizr.discord.bottowitzsch.player.trackidentifier;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.MessageCommand;

public interface TrackIdentifier {
	Optional<String> identify(String message, MessageCommand command);
}
