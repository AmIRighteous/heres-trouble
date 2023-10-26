package com.herestrouble;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private HashMap<Skill, Integer> exp = new HashMap<>();

	private Map<Integer, Boolean> friendsMap = new HashMap<>();

	private int heresTroubleTimer = 5;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

//	@Subscribe
//	public void onGameStateChanged(GameStateChanged gameStateChanged)
//	{
//		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
//		{
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
//		}
//	}

	@Subscribe
	public void onStatChanged(StatChanged e) throws IOException {
		exp.computeIfAbsent(e.getSkill(), k -> e.getXp());

		if (e.getXp() != exp.get(e.getSkill())) {
			soundEngine.playClip(Sound.WOO);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR) {
			friendsMap.clear();
		}
	}

	@Subscribe
	public void onGameTick(GameTick e) throws FileNotFoundException {
		if (heresTroubleTimer > 0) {
			heresTroubleTimer--;
		}
		else if (config.friendsList() || config.clanMembers()) {
			List<Player> players = client.getPlayers();
			for (Player p : players) {
				if (p.isFriend() && !friendsMap.containsKey(p.getId()) && config.friendsList()) {
					friendsMap.put(p.getId(), true);
					soundEngine.playClip(Sound.HERES_TROUBLE);
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
