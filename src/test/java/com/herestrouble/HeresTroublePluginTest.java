package com.herestrouble;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HeresTroublePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HeresTroublePlugin.class);
		RuneLite.main(args);
	}


}