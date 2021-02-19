package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QueueCommand implements Command {
	private final BottowitzschContext context;

	@Override
	public List<String> commands() {
		return Arrays.asList("!queue", "!q");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		final Mono<List<AudioTrack>> trackList = Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(context -> Mono.just(context.getTrackScheduler()))
			.flatMap(scheduler -> Mono.just(scheduler.getQueue()));

		return Mono.just(event.getMessage())
			.filter(Command::isMessageFromUser)
			.flatMap(Message::getChannel)
			.flatMap(channel -> channel.createEmbed(spec -> buildQueueMsg(spec, trackList)))
			.then();
	}

	private void buildQueueMsg(final EmbedCreateSpec spec, final Mono<List<AudioTrack>> trackList) {
		trackList.flatMapMany(Flux::fromIterable)
			.flatMap(audioTrack -> Mono.just(audioTrack.getInfo()))
			.flatMap(audioTrackInfo -> Mono.just(creatQueueEntry(audioTrackInfo)))
			.reduce(String::concat)
			.subscribe(spec::setDescription);
	}

	private String creatQueueEntry(final AudioTrackInfo audioTrackInfo) {
		return audioTrackInfo.title + "\n";
	}
}
