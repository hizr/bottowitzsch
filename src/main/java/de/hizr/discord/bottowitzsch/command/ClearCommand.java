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
public class ClearCommand implements Command {
	private final BottowitzschContext context;

	@Override
	public List<String> commands() {
		return Arrays.asList("!clear", "!c");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Mono.first(stopMusic(event))
			.then(clearPlaylist(event));
	}

	private Mono<Void> stopMusic(final MessageCreateEvent event) {
		return Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(guildContext -> Mono.just(guildContext.getAudioPlayer()))
			.doOnSuccess(AudioPlayer::stopTrack)
			.then();
	}

	private Mono<Void> clearPlaylist(final MessageCreateEvent event) {
		return Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(context -> Mono.just(context.getTrackScheduler()))
			.flatMap(scheduler -> Mono.just(scheduler.getQueue()))
			.doOnSuccess(List::clear)
			.then();
	}
}
