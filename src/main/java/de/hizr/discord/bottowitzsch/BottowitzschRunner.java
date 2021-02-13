package de.hizr.discord.bottowitzsch;

import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BottowitzschRunner implements CommandLineRunner {

	private final GatewayDiscordClient discordClient;

	@Override
	public void run(final String... args) throws Exception {
		discordClient.onDisconnect().block();
	}
}
