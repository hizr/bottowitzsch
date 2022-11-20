package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import de.hizr.discord.bottowitzsch.context.GuildContextService;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import de.hizr.discord.bottowitzsch.player.trackidentifier.extractor.CommandException;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RemoveCommand implements Command {
	public static final String DESCRIPTION_TEXT
		= "Removes a track from the playlist!\n\n"
		  + "Example:\n"
		  + "'!rm 1' -> Removes the next track if there is a next one.\n"
		  + "'!rm 3' -> Removes the third following track from the current one.";

	private final GuildContextService context;
	private final CommandHelper helper;

	@Override
	public List<String> messageHooks() {
		return Arrays.asList("!remove", "!rm");
	}

	@Override
	public String description() {
		return DESCRIPTION_TEXT;
	}

	@Override
	public Mono<Void> execute(final MessageCreateEvent event) {
		return Mono.just(event)
			.flatMap(MessageCreateEvent::getGuild)
			.flatMap(context::requestGuildContext)
			.flatMap(guildContext -> Mono.just(guildContext.getTrackScheduler()))
			.doOnNext(scheduler -> removeTrack(scheduler, event.getMessage()))
			.onErrorContinue((throwable, o) -> helper.handleError(throwable, "Something went wrong with remove-Command", event))
			.then()
			;
	}

	private void removeTrack(final AudioTrackScheduler scheduler, final Message message) {
		final String content = message.getContent();
		final String commandWithSpace = getCommandWithSpace(content);
		if (StringUtils.contains(content, commandWithSpace)) {
			final String plainMsg = StringUtils.remove(content, commandWithSpace).trim();

			try {
				final int index = Integer.parseInt(plainMsg) - 1;
				scheduler.remove(index);
			}
			catch (Exception e) {
				throw new CommandException(String.format("Cant remove index '%s'", content), e);
			}
		}
	}
}
