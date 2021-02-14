package de.hizr.discord.bottowitzsch.command;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelpMessageCommand implements MessageCommand {
	@Override
	public String command() {
		return "!help";
	}

	@Override
	public Mono<Void> execute(final Message eventMessage) {
		return Mono.just(eventMessage)
			.filter(MessageCommand::isMessageFromUser)
			.flatMap(Message::getChannel)
			.flatMap(channel -> channel.createMessage("Things to do today:\n - write a bot\n - eat lunch\n - play a game"))
			.then();
	}
}
