package de.hizr.discord.bottowitzsch.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import discord4j.common.util.Snowflake;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BottowitzschContext {
	public static final String NO_GUILD_ID_EXE_MSG = String.format("Requested %s requires guildId!", GuildContext.class.getSimpleName());

	private final GuildContextFactory guildContextFactory;
	private final Map<Snowflake, GuildContext> contextMap = new HashMap<>();

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
