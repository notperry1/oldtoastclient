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
	protected String MODULE_NAME;
	private boolean buttonDown = false;
	private boolean wasButtonDown = false;
	
	public void process() {
		System.out.println("No Process");
	}

	public void initKeybind(int code) {
		keyBinding = FabricKeyBinding.Builder
				.create(new Identifier("client", MODULE_NAME.toLowerCase()), InputUtil.Type.KEYSYM, code, "toastclient")
				.build();
		KeyBindingRegistry.INSTANCE.register(keyBinding);
		ClientTickCallback.EVENT.register(e -> {
			buttonDown = keyBinding.isPressed();
			if (buttonDown && !wasButtonDown) {
				Toggled = !Toggled;
				System.out.printf("%s: %s\n", MODULE_NAME, Toggled ? "ON" : "OFF");
			}
			wasButtonDown = buttonDown;
		});
	}

	public static MinecraftClient mc() {
		return MinecraftClient.getInstance();
	}
}
