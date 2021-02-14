package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.hizr.discord.bottowitzsch.scheduler.TrackScheduler;
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
	private final TrackScheduler scheduler;
	private final AudioPlayerManager playerManager;
	private final AudioProvider provider;

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

		Mono<Void> playMusic = Mono.justOrEmpty(event.getMessage().getContent())
			.map(content -> Arrays.asList(content.split(" ")))
			.doOnNext(command -> playerManager.loadItem(command.get(1), new AudioLoadResultHandler() {
				@Override
				public void trackLoaded(final AudioTrack track) {
					scheduler.play(track);
				}

				@Override
				public void playlistLoaded(final AudioPlaylist playlist) {
					for (AudioTrack track : playlist.getTracks()) {
						scheduler.play(track);
					}
				}

				@Override
				public void noMatches() {

				}

				@Override
				public void loadFailed(final FriendlyException exception) {

				}
			}))
			.then();
		return Mono.firstWithSignal(joinChannel).then(playMusic);
	}
}
