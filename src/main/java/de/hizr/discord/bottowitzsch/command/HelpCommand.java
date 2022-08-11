package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
	public static final String DESCRIPTION_TEXT = "List all commands and there Description";

	private final List<Command> commands;

	@Override
	public List<String> messageHooks() {
		return Arrays.asList("!help", "!h");
	}

	@Override
	public String description() {
		return DESCRIPTION_TEXT;
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent eventMessage) {
		return Mono.just(eventMessage.getMessage())
			.filter(Command::isMessageFromUser)
			.flatMap(Message::getChannel)
			.flatMap(channel -> channel.createEmbed(this::createHelpSpec))
			.then();
	}

	private void createHelpSpec(final EmbedCreateSpec spec) {
		spec.setTitle("All my commands:");

		commands.forEach(command -> {
			final String messageHook = command.messageHooks().stream().map(Object::toString).collect(Collectors.joining(", "));
			spec.addField(messageHook, command.description(), false);
		});
	}
}
