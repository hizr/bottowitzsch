package de.hizr.discord.bottowitzsch.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import de.hizr.discord.bottowitzsch.player.BottowitzschAudioLoadResultHandler;
import de.hizr.discord.bottowitzsch.player.trackidentifier.TrackIdentifier;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.voice.AudioProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayMessageCommand implements MessageCommand {

	private final AudioTrackScheduler scheduler;
	private final AudioPlayerManager playerManager;
	private final AudioProvider provider;
	private final TrackIdentifier trackIdentifier;

	@Override
	public String command() {
		return "!play";
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		final Mono<Void> joinChannel = Mono.justOrEmpty(event.getMember())
			.flatMap(Member::getVoiceState)
			.flatMap(VoiceState::getChannel)
			// join returns a VoiceConnection which would be required if we were
			// adding disconnection features, but for now we are just ignoring it.
			.flatMap(channel -> channel.join(spec -> spec.setProvider(provider)))
			.then();

		final Mono<Void> playMusic = Mono.justOrEmpty(event.getMessage().getContent())
			.doOnNext(command -> playerManager.loadItem(
				trackIdentifier.identify(event.getMessage().getContent(), this).orElse(""),
				new BottowitzschAudioLoadResultHandler(scheduler)))
			.then();

		return Mono.first(joinChannel).then(playMusic);
	}
}
