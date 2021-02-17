package de.hizr.discord.bottowitzsch.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StopMessageCommand implements MessageCommand {
	private final BottowitzschContext context;

	@Override
	public String command() {
		return "!stop";
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(guildContext -> Mono.just(guildContext.getAudioPlayer()))
			.doOnSuccess(AudioPlayer::stopTrack)
			.then();
	}
}
