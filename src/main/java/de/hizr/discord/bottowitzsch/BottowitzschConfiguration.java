package de.hizr.discord.bottowitzsch;

import java.util.List;
import java.util.Objects;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.hizr.discord.bottowitzsch.eventlistener.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class BottowitzschConfiguration {

	@Value("${token}")
	private String token;

	@Bean
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
		GatewayDiscordClient client = null;

		try {
			client = DiscordClientBuilder.create(token)
				.build()
				.login()
				.block();

			for (EventListener<T> listener : eventListeners) {
				Objects.requireNonNull(client).on(listener.getEventType())
					.flatMap(listener::execute)
					.onErrorResume(listener::handleError)
					.subscribe();
			}
		}
		catch (Exception exception) {
			log.error("Be sure to use a valid bot token!", exception);
		}

		return client;
	}

	@Bean
	public DefaultAudioPlayerManager audioPlayerManager() {
		final DefaultAudioPlayerManager playerManager = new DefaultAudioPlayerManager();
		// This is an optimization strategy that Discord4J can utilize.
		// It is not important to understand
		final AudioConfiguration configuration = playerManager.getConfiguration();
		configuration.setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
		configuration.setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
		configuration.setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);

		// Allow playerManager to parse remote sources like YouTube links
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		return playerManager;
	}
}