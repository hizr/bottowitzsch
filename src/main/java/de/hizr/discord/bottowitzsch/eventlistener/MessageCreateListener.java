package de.hizr.discord.bottowitzsch.eventlistener;

import java.util.List;

import de.hizr.discord.bottowitzsch.command.MessageCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageCreateListener implements EventListener<MessageCreateEvent> {

	private final List<MessageCommand> eventMessageCommands;

	@Override
	public Class<MessageCreateEvent> getEventType() {
		return MessageCreateEvent.class;
	}

	@Override
	public Mono<Void> execute(MessageCreateEvent event) {
		return Flux.fromIterable(eventMessageCommands)
			.filter(messageCommand -> event.getMessage().getContent().startsWith(messageCommand.command()))
			.flatMap(messageCommand -> messageCommand.execute(event.getMessage()))
			.then();
	}
}
