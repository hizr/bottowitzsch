package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.springframework.util.ResourceUtils.isUrl;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.Command;
import org.springframework.stereotype.Component;

@Component
public class YouTubeSearchExtractor implements LinkExtractor {

	@Override
	public Optional<String> extract(final String message, final Command command) {
		final String commandWithSpace = getCommandWithSpace(message, command);
		if (contains(message, commandWithSpace)) {
			final String plainMsg = remove(message, commandWithSpace);
			String value = isUrl(plainMsg) ? plainMsg.trim() : "ytsearch:" + plainMsg;
			return Optional.of(value);
		}
		else {
			return Optional.empty();
		}
	}

	private String getCommandWithSpace(final String message, final Command messageCommand) {
		return messageCommand.messageHooks().stream()
			.filter(command -> startsWith(message, command + " "))
			.findAny()
			.orElseThrow(() -> new CommandException(
				String.format("Command cant be extracted from message '%s'", message))
			);
	}
}
