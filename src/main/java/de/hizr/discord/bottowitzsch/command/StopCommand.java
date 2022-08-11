package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.hizr.discord.bottowitzsch.context.GuildContextService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StopCommand implements Command {
	public static final String DESCRIPTION_TEXT = "Stops the current track. Resume playlist/queue with !n/!next.";

	private final GuildContextService context;

	@Override
	public List<String> messageHooks() {
		return Arrays.asList("!stop", "!s");
	}

	@Override
	public String description() {
		return DESCRIPTION_TEXT;
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
