package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.MessageCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class YouTubeSearchExtractor implements LinkExtractor {

	@Override
	public Optional<String> extract(final String message, final MessageCommand command) {
		final String commandWithSpace = command.command() + " ";
		if (StringUtils.contains(message, commandWithSpace)) {
			final String plainMsg = StringUtils.remove(message, commandWithSpace);
			String value = ResourceUtils.isUrl(plainMsg) ? plainMsg : "ytsearch:" + plainMsg;
			return Optional.of(value);
		}
		else {
			return Optional.empty();
		}
	}
}
