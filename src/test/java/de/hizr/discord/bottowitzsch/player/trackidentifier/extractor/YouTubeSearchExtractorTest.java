package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.Command;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class YouTubeSearchExtractorTest {

	@Test
	public void testExtractor() {
		val playCommand = Mockito.mock(Command.class);
		Mockito.when(playCommand.commands()).thenReturn(Arrays.asList("!play", "!p"));

		val ytExtractor = new YouTubeSearchExtractor();
		// test
		final String expExtract = "https://www.youtube.com/watch?v=eH6tqXQNWUA";
		final Optional<String> extract = ytExtractor.extract("!play " + expExtract, playCommand);
		// assetion
		assertThat(extract.isPresent()).isTrue();
		assertThat(extract.get()).isEqualTo(" " + expExtract);
	}
}