package de.hizr.discord.bottowitzsch.command;

import java.util.List;

import de.hizr.discord.bottowitzsch.player.trackidentifier.extractor.CommandException;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public interface Command {
	List<String> messageHooks();

	String description();
	Mono<Void> execute(MessageCreateEvent event);

	static Boolean isMessageFromUser(final Message message) {
		return message.getAuthor().map(user -> !user.isBot()).orElse(false);
	}

	default String getCommandWithSpace(String message) {
		return messageHooks().stream()
			.filter(command -> StringUtils.startsWith(message, command + " "))
			.findAny()
			.orElseThrow(() -> new CommandException(
				String.format("Command cant be extracted from message '%s'", message))
			);
	}
}
