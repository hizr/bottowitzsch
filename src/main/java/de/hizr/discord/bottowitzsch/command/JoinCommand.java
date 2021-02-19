package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JoinCommand implements Command {
	private final BottowitzschContext context;

	@Override
	public List<String> commands() {
		return Arrays.asList("!join", "!j");
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		val guildContext = context.requestGuildContext(event.getGuildId());
		return Mono.justOrEmpty(event.getMember())
			.flatMap(Member::getVoiceState)
			.flatMap(VoiceState::getChannel)
			// join returns a VoiceConnection which would be required if we were
			// adding disconnection features, but for now we are just ignoring it.
			.flatMap(channel -> channel.join(spec -> spec.setProvider(guildContext.getAudioProvider())))
			.then();
	}
}
