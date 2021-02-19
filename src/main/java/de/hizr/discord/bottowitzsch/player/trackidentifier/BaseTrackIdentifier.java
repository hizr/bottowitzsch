package de.hizr.discord.bottowitzsch.player.trackidentifier;

import java.util.List;
import java.util.Optional;

import de.hizr.discord.bottowitzsch.command.Command;
import de.hizr.discord.bottowitzsch.player.trackidentifier.extractor.LinkExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseTrackIdentifier implements TrackIdentifier {
	private final List<LinkExtractor> extractors;

	@Override
	public Optional<String> identify(final String message, Command command) {
		Optional<String> indntifier = Optional.empty();
		for (LinkExtractor extractor : extractors) {
			indntifier = extractor.extract(message, command);
			if (indntifier.isPresent()) {
				break;
			}
		}
		return indntifier;
	}
}
