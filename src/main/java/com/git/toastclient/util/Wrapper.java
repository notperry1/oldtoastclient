package com.git.toastclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class Wrapper {

    public static MinecraftClient getMinecraft() {return MinecraftClient.getInstance(); }

    public static PlayerEntity getPlayer() {
        return getMinecraft().player;
    }

}
