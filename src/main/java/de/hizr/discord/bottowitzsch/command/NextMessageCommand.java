package de.hizr.discord.bottowitzsch.command;

import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import de.hizr.discord.bottowitzsch.context.GuildContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NextMessageCommand implements MessageCommand {
	private final BottowitzschContext context;

	@Override
	public String command() {
		return "!next";
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		final GuildContext guildContext = context.requestGuildContext(event.getGuildId());
		guildContext.getTrackScheduler().skip();
		return Mono.just(event).then();
	}
}
