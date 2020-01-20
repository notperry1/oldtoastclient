package com.git.toastclient.module;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public abstract class Module {

	private FabricKeyBinding keyBinding;
	public volatile boolean Toggled = false;
	private long lastPress = 0;
	protected String MODULE_NAME;
	
	public void process() {
		System.out.println("No Process");
	}

	public void initKeybind(int code) {
		keyBinding = FabricKeyBinding.Builder
				.create(new Identifier("client", MODULE_NAME.toLowerCase()), InputUtil.Type.KEYSYM, code, "toastclient")
				.build();
		KeyBindingRegistry.INSTANCE.register(keyBinding);
		ClientTickCallback.EVENT.register(e -> {
			if (keyBinding.isPressed()) {
				if ((System.currentTimeMillis() - lastPress) > 50) {
					Toggled = !Toggled;
					lastPress = System.currentTimeMillis();
				}
			}
		});
	}

	public static MinecraftClient mc() {
		return MinecraftClient.getInstance();
	}
}
