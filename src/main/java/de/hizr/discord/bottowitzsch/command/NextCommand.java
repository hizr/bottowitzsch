package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NextCommand implements Command {
	private final BottowitzschContext context;

	@Override
	public List<String> commands() {
		return Arrays.asList("!next", "!n");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(guildContext -> Mono.just(guildContext.getTrackScheduler()))
			.doOnSuccess(AudioTrackScheduler::skip)
			.then();
	}
}
