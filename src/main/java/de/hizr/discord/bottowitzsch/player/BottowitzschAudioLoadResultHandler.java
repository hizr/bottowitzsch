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
		if (playlist.getTracks().size() > 0) {
			final AudioTrack track = playlist.getTracks().get(0);
			scheduler.play(track);
		}
	}

	@Override
	public void noMatches() {
		// nothing to show... it just cant find something to play
	}

	@Override
	public void loadFailed(final FriendlyException exception) {
		// nothing to show... it just friendly failed to load the track
	}
}
