package com.git.toastclient;

import com.git.toastclient.module.ModuleManager;
import com.git.toastclient.module.modules.ClickGUI;
import com.git.toastclient.module.modules.Safewalk;
import com.git.toastclient.module.modules.Scaffold;
import net.fabricmc.api.ModInitializer;

import java.util.Map;

public class ClientMain implements ModInitializer {

	public static final String APP_ID = "669916916290420736";
	public static final String SHRUG = "¯\\_(ツ)_/¯";

	private final Map m = new ModuleManager().Modules;
//	public static Map<String, Module> Modules = new TreeMap<String, Module>(String.CASE_INSENSITIVE_ORDER);


	@Override
	public void onInitialize() {

		m.put("Scaffold", new Scaffold());
		m.put("Safewalk", new Safewalk());
		m.put("ClickGUI", new ClickGUI());

		DiscordPresence.start();
		System.out.println("Mushroom client booting up.");
	}
}

