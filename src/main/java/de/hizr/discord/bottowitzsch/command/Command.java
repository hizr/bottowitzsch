package de.hizr.discord.bottowitzsch.command;

import java.util.List;

import de.hizr.discord.bottowitzsch.player.trackidentifier.extractor.BottowitzschCommandException;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public interface Command {
	List<String> commands();
	Mono<Void> execute(MessageCreateEvent event);

	static Boolean isMessageFromUser(final Message message) {
		return message.getAuthor().map(user -> !user.isBot()).orElse(false);
	}
	default String getCommandWithSpace(String message) {
		return commands().stream()
			.filter(command -> StringUtils.startsWith(message, command + " "))
			.findAny()
			.orElseThrow(() -> new BottowitzschCommandException(
				String.format("Command cant be extracted from message '%s'", message))
			);
	}
}
