package com.herestrouble;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Here's Trouble")
public interface HeresTroubleConfig extends Config
{
	@ConfigItem(
			keyName = "friends",
			name = "Friends List",
			description = "Play sound when you see a player on your friends list."
	)
	default boolean friendsList() {
		return true;
	}

	@ConfigItem(
			keyName = "clan",
			name = "Clan Members",
			description = "Play sound when you see a player within your clan."
	)
	default boolean clanMembers() {
		return true;
	}

	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "Volume of sounds generated from plugin."
	)
	default int pluginVolume() {
		return 100;
	}

}
