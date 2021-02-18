package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import de.hizr.discord.bottowitzsch.context.GuildContext;
import de.hizr.discord.bottowitzsch.player.BottowitzschAudioLoadResultHandler;
import de.hizr.discord.bottowitzsch.player.trackidentifier.TrackIdentifier;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayMessageCommand implements MessageCommand {

	private final BottowitzschContext context;
	private final TrackIdentifier trackIdentifier;

	@Override
	public List<String> commands() {
		return Arrays.asList("!play", "!p");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		final GuildContext guildContext = context.requestGuildContext(event.getGuildId());

		final Mono<Void> joinChannel = Mono.justOrEmpty(event.getMember())
			.flatMap(Member::getVoiceState)
			.flatMap(VoiceState::getChannel)
			// join returns a VoiceConnection which would be required if we were
			// adding disconnection features, but for now we are just ignoring it.
			.flatMap(channel -> channel.join(spec -> spec.setProvider(guildContext.getAudioProvider())))
			.doOnSuccess(guildContext::setVoiceConnection)
			.then();

		final Mono<Void> playMusic = Mono.justOrEmpty(event.getMessage().getContent())
			.doOnNext(command -> guildContext.getAudioPlayerManager().loadItemOrdered(
				guildContext,
				trackIdentifier.identify(event.getMessage().getContent(), this).orElse(""),
				new BottowitzschAudioLoadResultHandler(guildContext.getTrackScheduler())))
			.then();

		return Mono.first(joinChannel).then(playMusic);
	}
}
