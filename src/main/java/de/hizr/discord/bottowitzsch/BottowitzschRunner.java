package de.hizr.discord.bottowitzsch;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.hizr.discord.bottowitzsch.player.LavaPlayerAudioProvider;
import discord4j.core.GatewayDiscordClient;
import discord4j.voice.AudioProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BottowitzschRunner implements CommandLineRunner {

	private final GatewayDiscordClient discordClient;

	@Override
	public void run(final String... args) throws Exception {
		// Creates AudioPlayer instances and translates URLs to AudioTrack instances
		final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

		// This is an optimization strategy that Discord4J can utilize.
		// It is not important to understand
		playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

		// Allow playerManager to parse remote sources like YouTube links
		AudioSourceManagers.registerRemoteSources(playerManager);

		// Create an AudioPlayer so Discord4J can receive audio data
		final AudioPlayer player = playerManager.createPlayer();

		// We will be creating LavaPlayerAudioProvider in the next step
		AudioProvider provider = new LavaPlayerAudioProvider(player);

		// block the application to instantly stop
		discordClient.onDisconnect().block();
	}
}
