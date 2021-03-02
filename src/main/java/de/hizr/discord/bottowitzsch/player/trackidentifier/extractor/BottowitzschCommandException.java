package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

public class BottowitzschCommandException extends RuntimeException {
	public BottowitzschCommandException(final String message) {
		super(message);
	}

	public BottowitzschCommandException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
