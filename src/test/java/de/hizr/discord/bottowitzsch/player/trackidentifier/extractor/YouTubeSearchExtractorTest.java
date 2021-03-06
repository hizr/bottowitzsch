package de.hizr.discord.bottowitzsch.player.trackidentifier.extractor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import de.hizr.discord.bottowitzsch.command.Command;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class YouTubeSearchExtractorTest {

	public static Stream<Arguments> testExtractor() {
		return Stream.of(
			Arguments.of("!play https://www.youtube.com/watch?v=eH6tqXQNWUA", "https://www.youtube.com/watch?v=eH6tqXQNWUA"),
			Arguments.of("!p https://www.youtube.com/watch?v=eH6tqXQNWUA", "https://www.youtube.com/watch?v=eH6tqXQNWUA"),
			Arguments.of("!p never gonna give you up", "ytsearch: never gonna give you up"),
			Arguments.of("!play never gonna give you up", "ytsearch: never gonna give you up")
		);
	}

	@ParameterizedTest
	@MethodSource
	public void testExtractor(String msg, String expOutcome) {
		val playCommand = Mockito.mock(Command.class);
		Mockito.when(playCommand.commands()).thenReturn(Arrays.asList("!play", "!p"));

		val unitToTest = new YouTubeSearchExtractor();
		// test
		final Optional<String> extract = unitToTest.extract(msg, playCommand);
		// assetion
		assertThat(extract.isPresent()).isTrue();
		assertThat(extract.get()).isEqualTo(expOutcome);
	}
}