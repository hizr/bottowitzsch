package de.hizr.discord.bottowitzsch.context;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import discord4j.voice.AudioProvider;
import discord4j.voice.VoiceConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class GuildContext {
	@Setter
	private VoiceConnection voiceConnection;
	private final AudioTrackScheduler trackScheduler;
	private final AudioProvider audioProvider;
	private final AudioPlayer audioPlayer;
	private final AudioPlayerManager audioPlayerManager;
}
