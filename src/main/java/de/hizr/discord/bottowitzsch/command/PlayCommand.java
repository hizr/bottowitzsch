package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import de.hizr.discord.bottowitzsch.context.GuildContext;
import de.hizr.discord.bottowitzsch.player.BottowitzschAudioLoadResultHandler;
import de.hizr.discord.bottowitzsch.player.trackidentifier.TrackIdentifier;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayCommand implements Command {

	public static final String DESCRIPTION
		= "Add's a track to the current playlist. If there is no playlist it will create one.\n"
		  + "You can add a direct youtube link or search by the complete phrase.\n\n"
		  + "Example:\n"
		  + "'!p never gonna give you up' -> Searches on youtube for a video and plays the first result of the search.\n"
		  + "'!p https://www.youtube.com/watch?v=dQw4w9WgXcQ' -> Plays exactly this video!";

	private final BottowitzschContext context;
	private final TrackIdentifier trackIdentifier;
	private final CommandHelper helper;

	@Override
	public List<String> commands() {
		return Arrays.asList("!play", "!p");
	}

	@Override
	public String description() {
		return DESCRIPTION;
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		val guildContext = context.requestGuildContext(event.getGuildId());

		return Mono.first(joinChannel(event, guildContext))
			.then(playMusic(event, guildContext))
			.onErrorContinue((throwable, o) -> helper.handleError(throwable, "Something went wrong with play-Command", event))
			// .then(postTrack(event, guildContext))
			;
	}

	private Mono<Void> postTrack(final MessageCreateEvent event, final GuildContext guildContext) {
		return Mono.justOrEmpty(event.getMessage())
			.flatMap(Message::getChannel)
			.flatMap(messageChannel -> messageChannel.createEmbed(spec -> createFounTrackMessage(spec, guildContext)))
			.then();
	}

	private void createFounTrackMessage(final EmbedCreateSpec spec, final GuildContext guildContext) {
		final AudioTrack audioTrack = guildContext.getTrackScheduler().getQueue().get(0);
		final AudioTrackInfo info = audioTrack.getInfo();
		final String uri = info.uri;
		final String title = info.title;

		spec.setDescription(title + " " + uri);
	}

	private Mono<Void> joinChannel(final MessageCreateEvent event, final GuildContext guildContext) {
		return Mono.justOrEmpty(event.getMember())
			.flatMap(Member::getVoiceState)
			.flatMap(VoiceState::getChannel)
			.flatMap(channel -> channel.join(spec -> spec.setProvider(guildContext.getAudioProvider())))
			.doOnSuccess(guildContext::setVoiceConnection)
			.then();
	}

	private Mono<Void> playMusic(final MessageCreateEvent event, final GuildContext guildContext) {
		return Mono.justOrEmpty(event.getMessage().getContent())
			.doOnNext(command -> guildContext.getAudioPlayerManager().loadItemOrdered(
				guildContext,
				trackIdentifier.identify(event.getMessage().getContent(), this).orElse(""),
				new BottowitzschAudioLoadResultHandler(guildContext.getTrackScheduler())))
			.then();
	}
}
