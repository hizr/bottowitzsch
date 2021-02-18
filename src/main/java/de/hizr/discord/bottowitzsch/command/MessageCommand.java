package de.hizr.discord.bottowitzsch.command;

import java.util.List;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface MessageCommand {
	List<String> commands();
	Mono<Void> execute(MessageCreateEvent event);

	static Boolean isMessageFromUser(final Message message) {
		return message.getAuthor().map(user -> !user.isBot()).orElse(false);
	}
}
