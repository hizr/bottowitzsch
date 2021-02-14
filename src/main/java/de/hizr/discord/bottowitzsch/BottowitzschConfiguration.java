package de.hizr.discord.bottowitzsch;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.hizr.discord.bottowitzsch.eventlistener.EventListener;
import de.hizr.discord.bottowitzsch.player.LavaPlayerAudioProvider;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.voice.AudioProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BottowitzschConfiguration {

	private static final Logger log = LoggerFactory.getLogger(BottowitzschConfiguration.class);

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
				client.on(listener.getEventType())
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
	@DependsOn("audioPlayer")
	public AudioProvider audioProvider(final AudioPlayer player) {
		// We will be creating LavaPlayerAudioProvider in the next step
		return new LavaPlayerAudioProvider(player);
	}

	@Bean
	@DependsOn("audioPlayerManager")
	public AudioPlayer audioPlayer(final AudioPlayerManager playerManager) {
		// This is an optimization strategy that Discord4J can utilize.
		// It is not important to understand
		playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

		// Allow playerManager to parse remote sources like YouTube links
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		// Create an AudioPlayer so Discord4J can receive audio data
		return playerManager.createPlayer();
	}

	@Bean
	public AudioPlayerManager audioPlayerManager() {
		// Creates AudioPlayer instances and translates URLs to AudioTrack instances
		return new DefaultAudioPlayerManager();
	}
}