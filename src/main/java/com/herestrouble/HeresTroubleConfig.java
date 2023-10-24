package com.herestrouble;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface HeresTroubleConfig extends Config
{
	@ConfigItem(
		keyName = "greeting",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}

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

}
