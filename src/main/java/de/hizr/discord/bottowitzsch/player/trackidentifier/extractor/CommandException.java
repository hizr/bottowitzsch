package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

public class CommandException extends RuntimeException {
	public CommandException(final String message) {
		super(message);
	}

	public CommandException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
