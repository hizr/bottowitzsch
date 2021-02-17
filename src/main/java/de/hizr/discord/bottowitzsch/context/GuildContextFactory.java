package de.hizr.discord.bottowitzsch.context;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import de.hizr.discord.bottowitzsch.player.LavaPlayerAudioProvider;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class GuildContextFactory {

	public GuildContext of() {
		val playerManager = new DefaultAudioPlayerManager();
		val audioPlayer = audioPlayer(playerManager);
		return new GuildContext(
			new AudioTrackScheduler(audioPlayer),
			new LavaPlayerAudioProvider(audioPlayer),
			audioPlayer,
			playerManager);
	}

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
}
