package com.git.toastclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class Wrapper {

    public static MinecraftClient getMinecraft() {return MinecraftClient.getInstance(); }

    public static PlayerEntity getPlayer() {
        return getMinecraft().player;
    }

    public static World getWorld() {
        return getMinecraft().world;
    }

//    public static int getKey(String keyname) {
//        return Keyboard.getKeyIndex(keyname.toUpperCase());
//    }


}
