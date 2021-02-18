package de.hizr.discord.bottowitzsch.eventlistener;

import java.util.List;

import de.hizr.discord.bottowitzsch.command.MessageCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Flux.fromIterable(eventMessageCommands)
			.filter(messageCommand -> isMatchingCommand(event.getMessage().getContent(), messageCommand))
			.flatMap(messageCommand -> messageCommand.execute(event))
			.then();
	}

	private boolean isMatchingCommand(final String message, final MessageCommand messageCommand) {
		return messageCommand.commands()
			.stream()
			.anyMatch(command -> StringUtils.startsWith(message, command));
	}
}
