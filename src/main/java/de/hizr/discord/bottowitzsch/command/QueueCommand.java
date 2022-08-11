package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.hizr.discord.bottowitzsch.context.GuildContextService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QueueCommand implements Command {
	public static final String DESCRIPTION_TEXT = "Prints the current playlist/queue.";

	private final GuildContextService context;
	private int trackNumber;

	@Override
	public List<String> messageHooks() {
		return Arrays.asList("!queue", "!q");
	}

	@Override
	public String description() {
		return DESCRIPTION_TEXT;
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Mono.just(event.getMessage())
			.filter(Command::isMessageFromUser)
			.flatMap(Message::getChannel)
			.flatMap(channel -> channel.createEmbed(spec -> {
				final Mono<AudioTrackInfo> actualTrack = getActualTrack(event);
				final Mono<List<AudioTrack>> trackList = getTrackList(event);
				buildQueueMsg(spec, actualTrack, trackList);
			}))
			.then();
	}

	private Mono<AudioTrackInfo> getActualTrack(final MessageCreateEvent event) {
		return Mono.justOrEmpty(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(guildContext -> Mono.justOrEmpty(guildContext.getAudioPlayer().getPlayingTrack()))
			.flatMap(audioTrack -> Mono.justOrEmpty(audioTrack.getInfo()));
	}

	private Mono<List<AudioTrack>> getTrackList(final MessageCreateEvent event) {
		return Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(guildContext -> Mono.just(guildContext.getTrackScheduler()))
			.flatMap(scheduler -> Mono.just(scheduler.getQueue()));
	}

	private void buildQueueMsg(
		final EmbedCreateSpec spec,
		final Mono<AudioTrackInfo> actualTrack,
		final Mono<List<AudioTrack>> trackList
	) {
		actualTrack.subscribe(trackInfo -> {
			spec.setTitle("Playing: " + getTitle(trackInfo) + " - " + getDurationInMin(trackInfo));
			spec.setUrl(trackInfo.uri);
		});

		trackNumber = 1;

		trackList.flatMapMany(Flux::fromIterable)
			.flatMap(audioTrack -> Mono.just(audioTrack.getInfo()))
			.flatMap(audioTrackInfo -> Mono.just(creatQueueEntry(audioTrackInfo)))
			.reduce(String::concat)
			.subscribe(spec::setDescription);
	}

	private String getTitle(final AudioTrackInfo audioTrackInfo) {
		final int maxSize = 50;
		return audioTrackInfo.title.length() > maxSize
			? StringUtils.substring(audioTrackInfo.title, 0, maxSize) + "..."
			: audioTrackInfo.title;
	}

	private String getDurationInMin(final AudioTrackInfo audioTrackInfo) {
		final long seconds = (audioTrackInfo.length / 1000) % 60;
		String tmpSeconds = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
		return (audioTrackInfo.length / 1000) / 60 + ":" + tmpSeconds;
	}

	private String creatQueueEntry(final AudioTrackInfo audioTrackInfo) {
		return trackNumber++ + " - [" + getTitle(audioTrackInfo) + "](" + audioTrackInfo.uri + ") - [" + getDurationInMin(audioTrackInfo) + "]\n";
	}
}
