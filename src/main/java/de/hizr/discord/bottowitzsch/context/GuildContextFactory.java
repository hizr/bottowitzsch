package de.hizr.discord.bottowitzsch.context;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import de.hizr.discord.bottowitzsch.player.LavaPlayerAudioProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuildContextFactory {
	private final DefaultAudioPlayerManager audioPlayerManager;

	public GuildContext of() {
		val audioPlayer = audioPlayerManager.createPlayer();
		return new GuildContext(
			new AudioTrackScheduler(audioPlayer),
			new LavaPlayerAudioProvider(audioPlayer),
			audioPlayer,
			audioPlayerManager);
	}
}
