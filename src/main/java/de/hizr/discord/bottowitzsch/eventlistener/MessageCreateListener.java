package de.hizr.discord.bottowitzsch.eventlistener;

import java.util.List;

import de.hizr.discord.bottowitzsch.command.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageCreateListener implements EventListener<MessageCreateEvent> {

	private final List<Command> eventCommands;

	@Override
	public Class<MessageCreateEvent> getEventType() {
		return MessageCreateEvent.class;
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Flux.fromIterable(eventCommands)
			.filter(messageCommand -> isMatchingCommand(event.getMessage().getContent(), messageCommand))
			.flatMap(messageCommand -> messageCommand.execute(event))
			.then();
	}

	private boolean isMatchingCommand(final String message, final Command messageCommand) {
		return messageCommand.messageHooks()
			.stream()
			.anyMatch(messageHook -> StringUtils.startsWith(message, messageHook));
	}
}
