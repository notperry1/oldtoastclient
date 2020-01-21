package com.git.toastclient;

import com.git.toastclient.module.Module;
import com.git.toastclient.module.modules.ClickGUI;
import com.git.toastclient.module.modules.Safewalk;
import com.git.toastclient.module.modules.Scaffold;
import net.fabricmc.api.ModInitializer;

import java.util.Map;
import java.util.TreeMap;

public class ClientMain implements ModInitializer {

	public static Map<String, Module> Modules = new TreeMap<String, Module>(String.CASE_INSENSITIVE_ORDER);


	@Override
	public void onInitialize() {

		Modules.put("Scaffold", new Scaffold());
		Modules.put("Safewalk", new Safewalk());
		Modules.put("ClickGUI", new ClickGUI());

		System.out.println("Welcome to the best client1010101!");
	}
}

