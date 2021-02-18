package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelpMessageCommand implements MessageCommand {
	@Override
	public List<String> commands() {
		return Arrays.asList("!help", "!h");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent eventMessage) {
		return Mono.just(eventMessage.getMessage())
			.filter(MessageCommand::isMessageFromUser)
			.flatMap(Message::getChannel)
			.flatMap(channel -> channel.createMessage("Commands:\n !play <youtube-link>\n !join your voice channel"))
			.then();
	}
}
