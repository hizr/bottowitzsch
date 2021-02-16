package de.hizr.discord.bottowitzsch.context;

import de.hizr.discord.bottowitzsch.player.AudioTrackScheduler;
import discord4j.voice.VoiceConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class GuildContext {
	@Setter
	private VoiceConnection voiceChannel;
	private final AudioTrackScheduler trackScheduler;
}
