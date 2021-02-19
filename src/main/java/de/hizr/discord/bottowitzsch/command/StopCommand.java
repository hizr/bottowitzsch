package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StopCommand implements Command {
	private final BottowitzschContext context;

	@Override
	public List<String> commands() {
		return Arrays.asList("!stop", "!s");
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
