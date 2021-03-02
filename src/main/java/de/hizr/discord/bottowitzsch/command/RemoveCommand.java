package de.hizr.discord.bottowitzsch.command;

import java.util.Arrays;
import java.util.List;

import de.hizr.discord.bottowitzsch.context.BottowitzschContext;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import de.hizr.discord.bottowitzsch.player.trackidentifier.extractor.BottowitzschCommandException;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveCommand implements Command {
	private final BottowitzschContext context;
	private final CommandHelper helper;

	@Override
	public List<String> commands() {
		return Arrays.asList("!rm", "!remove");
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
				throw new BottowitzschCommandException(String.format("Cant remove index '%s'", content), e);
			}
		}
	}
}
