package de.hizr.discord.bottowitzsch.command;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PlayMessageCommand implements MessageCommand {
	@Override
	public String command() {
		return "!play";
	}

	@Override
	public Mono<Void> execute(final Message msg) {
		return Mono.just(msg)
			.filter(MessageCommand::isMessageFromUser)
			.flatMap(Message::getChannel)
			.flatMap(channel -> channel.createMessage("Should play a song"))
			.then();
	}
}
