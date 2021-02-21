package de.hizr.discord.bottowitzsch.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandHelper {

	public void handleError(final Throwable throwable, final String msg, final MessageCreateEvent event) {
		final String warnMessage = String.format("%s on Guild '%s' with Message from User '%s'",
		                                         msg,
		                                         "guildName here" /* Objects.requireNonNull(event.getGuild().block()).getName() */,
		                                         event.getMessage().getUserData().username());
		log.warn(warnMessage, throwable);
	}
}
