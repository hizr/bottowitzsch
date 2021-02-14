package de.hizr.discord.bottowitzsch.eventlistener;

import java.util.List;

import de.hizr.discord.bottowitzsch.command.MessageCommand;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageUpdateListener implements EventListener<MessageUpdateEvent> {

	private final List<MessageCommand> messageCommands;

	@Override
	public Class<MessageUpdateEvent> getEventType() {
		return MessageUpdateEvent.class;
	}

	@Override
	public Mono<Void> execute(MessageUpdateEvent event) {
		for (MessageCommand messageCommand : messageCommands) {
			return Mono.just(event)
				.filter(MessageUpdateEvent::isContentChanged)
				.flatMap(MessageUpdateEvent::getMessage)
				.filter(message -> message.getContent().startsWith(messageCommand.command()))
				.flatMap(messageCommand::execute);
		}
		throw new RuntimeException("Cannot Find Command...");
	}
}