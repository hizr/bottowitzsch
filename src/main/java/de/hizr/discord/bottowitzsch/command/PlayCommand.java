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
import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayCommand implements Command {

	private final BottowitzschContext context;
	private final TrackIdentifier trackIdentifier;

	@Override
	public List<String> commands() {
		return Arrays.asList("!play", "!p");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		val guildContext = context.requestGuildContext(event.getGuildId());
		return Mono.first(joinChannel(event, guildContext))
			.then(playMusic(event, guildContext));
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
