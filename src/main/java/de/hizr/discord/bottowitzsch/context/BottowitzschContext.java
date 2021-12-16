package de.hizr.discord.bottowitzsch.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class BottowitzschContext {
	public static final String NO_GUILD_ID_EXE_MSG = String.format("Requested %s requires guildId!", GuildContext.class.getSimpleName());

	private final GuildContextFactory guildContextFactory;
	private final Map<Snowflake, GuildContext> contextMap = new HashMap<>();

	public Mono<GuildContext> requestGuildContext(Guild guild) {
		return Mono.just(requestGuildContext(Optional.of(guild.getId())));
	}

	public GuildContext requestGuildContext(Optional<Snowflake> guildId) {
		if (guildId.isPresent()) {
			val rawGuildId = guildId.get();
			if (contextMap.containsKey(rawGuildId)) {
				log.debug("Requested Context {}", rawGuildId);
				return contextMap.get(rawGuildId);
			}
			else {
				val guildContext = guildContextFactory.of();
				contextMap.put(rawGuildId, guildContext);
				return guildContext;
			}
		}
		else {
			throw new BottowitzschContextException(NO_GUILD_ID_EXE_MSG);
		}
	}
}
