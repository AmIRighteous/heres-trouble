package com.herestrouble;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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

	private Map<Integer, Boolean> friendsMap = new HashMap<>();

	private int heresTroubleTimer = 15;

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
	public void onGameTick(GameTick e) {
		if (heresTroubleTimer > 0) {
			heresTroubleTimer--;
		}
		else if (config.friendsList() || config.clanMembers()) {
			List<Player> players = client.getPlayers();
			for (Player p : players) {
				if (p.isFriend() && !friendsMap.containsKey(p.getId())) {
					friendsMap.put(p.getId(), true);
					client.getLocalPlayer().setOverheadText("Here's trouble");
					client.getLocalPlayer().setOverheadCycle(200);
				}
			}
			heresTroubleTimer = 15;
		}
	}

	@Provides
	HeresTroubleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HeresTroubleConfig.class);
	}
}
