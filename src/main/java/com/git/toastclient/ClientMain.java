package com.git.toastclient;

import com.git.toastclient.modules.Module;
import com.git.toastclient.modules.Scaffold;
import net.fabricmc.api.ModInitializer;

import java.util.Map;
import java.util.TreeMap;

public class ClientMain implements ModInitializer {

	public static Map<String, Module> Modules = new TreeMap<String, Module>(String.CASE_INSENSITIVE_ORDER);


	@Override
	public void onInitialize() {

		Modules.put("Scaffold", new Scaffold());

		System.out.println("Welcome to the best client1010101!");
	}
}

