package com.herestrouble;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.FileNotFoundException;
import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "Here's Trouble"
)
public class HeresTroublePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HeresTroubleConfig config;

	@Inject
	private SoundEngine soundEngine;

	private Set<String> friendsSeen = new HashSet<>();

	private int heresTroubleTimer = 5;

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR) {
			friendsSeen.clear();
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage c) {
		if (c.getType().equals(ChatMessageType.LOGINLOGOUTNOTIFICATION)) {
			String text = c.getMessage();
			if (text.contains(" has logged out.")) {
				text = text.replace(" has logged out.", "");
				String name = text.replaceAll("\\P{Print}", " "); //spaces sometimes read as unicode, so replace unicode chars with spaces
				friendsSeen.remove(name);
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick e) {
		if (heresTroubleTimer > 0) {
			heresTroubleTimer--;
		}

		else if (config.friendsList() || config.clanMembers()) {
			List<Player> players = client.getPlayers();
			for (Player p : players) {
				if (((p.isFriend() && config.friendsList()) || (p.isClanMember() && config.clanMembers())) && !friendsSeen.contains(p.getName()) && p != client.getLocalPlayer()) {
					friendsSeen.add(p.getName());
					int troubleNum = (int) Math.ceil(Math.random()*3);
					soundEngine.playClip(Sound.getSound(troubleNum));
					client.getLocalPlayer().setOverheadText("Here's trouble");
					p.setOverheadText("Here's trouble");
					p.setOverheadCycle(200);
					client.getLocalPlayer().setOverheadCycle(200);
				}
			}
			heresTroubleTimer = 5;
		}
		else {
			heresTroubleTimer = 5;
		}
	}

	@Provides
	HeresTroubleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HeresTroubleConfig.class);
	}
}
