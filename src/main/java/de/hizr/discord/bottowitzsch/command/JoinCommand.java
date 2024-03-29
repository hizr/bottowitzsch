package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import de.hizr.discord.bottowitzsch.context.GuildContextService;
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
	public static final String DESCRIPTION_TEXT = "Let me join your current voice channel.";

	private final GuildContextService context;

	@Override
	public List<String> messageHooks() {
		return Arrays.asList("!join", "!j");
	}

	@Override
	public String description() {
		return DESCRIPTION_TEXT;
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		val guildContext = context.requestGuildContext(event.getGuildId());
		return Mono.justOrEmpty(event.getMember())
			.flatMap(Member::getVoiceState)
			.flatMap(VoiceState::getChannel)
			.flatMap(channel -> channel.join(spec -> spec.setProvider(guildContext.getAudioProvider())))
			.then();
	}
}
