package com.git.toastclient.module;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public abstract class Module {

	private FabricKeyBinding keyBinding;
	public volatile boolean Toggled = false;

	public void process() {
		System.out.println("No Process");
	}

	public void initKeybind(String name, int code) {

		keyBinding = FabricKeyBinding.Builder
				.create(new Identifier("client", name.toLowerCase()), InputUtil.Type.KEYSYM, code, "toastclient")
				.build();
		KeyBindingRegistry.INSTANCE.register(keyBinding);

		ClientTickCallback.EVENT.register(e -> {
			if (keyBinding.isPressed())
				Toggled = !Toggled;
		});
	}
}
