package com.git.toastclient;

import com.git.toastclient.module.Module;
import com.git.toastclient.module.modules.Safewalk;
import com.git.toastclient.module.modules.Scaffold;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.TreeMap;

public class ClientMain implements ModInitializer {

	public static Map<String, Module> Modules = new TreeMap<String, Module>(String.CASE_INSENSITIVE_ORDER);


	@Override
	public void onInitialize() {

		net.fabricmc.fabric.api.event.network.S2CPacketTypeCallback.REGISTERED.register(e -> {
			System.out.println(e.getClass().getName());
		});

		net.fabricmc.fabric.api.event.network.C2SPacketTypeCallback.REGISTERED.register((e, f) -> {
			System.out.println(e.getDisplayName());
			for (Identifier i : f) {
				System.out.println(i.toString());
			}
		});
		
		Modules.put("Scaffold", new Scaffold());
		Modules.put("Safewalk", new Safewalk());

		System.out.println("Welcome to the best client1010101!");
	}
}

