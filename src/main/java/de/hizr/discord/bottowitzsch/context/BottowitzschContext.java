package de.hizr.discord.bottowitzsch.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import discord4j.common.util.Snowflake;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class BottowitzschContext {
	public static final String NO_GUILD_ID_EXE_MSG = String.format("Requested %s requires guildId!", GuildContext.class.getSimpleName());

	private final AudioPlayer player;

	private final Map<Snowflake, GuildContext> contextMap = new HashMap<>();

	public GuildContext requestGuildContext(Optional<Snowflake> guildId) {
		if (guildId.isPresent()) {
			final Snowflake rawGuildId = guildId.get();
			if (contextMap.containsKey(rawGuildId)) {
				log.debug("Requested Context {}", rawGuildId);
				return contextMap.get(rawGuildId);
			}
			else {
				GuildContext guildContext = new GuildContext(new AudioTrackScheduler(player));
				contextMap.put(rawGuildId, guildContext);
				return guildContext;
			}
		}
		else {
			throw new BottowitzschContextException(NO_GUILD_ID_EXE_MSG);
		}
	}
}
