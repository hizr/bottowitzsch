package de.hizr.discord.bottowitzsch.player.trackidentifier;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.Command;

public interface TrackIdentifier {
	Optional<String> identify(String message, Command command);
}
