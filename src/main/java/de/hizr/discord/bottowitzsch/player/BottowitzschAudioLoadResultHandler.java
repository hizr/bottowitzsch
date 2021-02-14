package de.hizr.discord.bottowitzsch.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BottowitzschAudioLoadResultHandler implements AudioLoadResultHandler {
	private final AudioTrackScheduler scheduler;

	@Override
	public void trackLoaded(final AudioTrack track) {
		scheduler.play(track);
	}

	@Override
	public void playlistLoaded(final AudioPlaylist playlist) {
		for (AudioTrack track : playlist.getTracks()) {
			scheduler.play(track);
		}
	}

	@Override
	public void noMatches() {

	}

	@Override
	public void loadFailed(final FriendlyException exception) {

	}
}
