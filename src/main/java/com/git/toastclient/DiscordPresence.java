package com.git.toastclient;


import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordPresence {

    //    669916916290420736
public static DiscordRichPresence presence;
    private static boolean hasStarted;
    private static final DiscordRPC rpc;
    private static String details;
    private static String state;

    public static void start() {
        if (DiscordPresence.hasStarted) return;
        DiscordPresence.hasStarted = true;

        final DiscordEventHandlers handlers = new DiscordEventHandlers();
//        handlers.disconnected = ((var1, var2) -> KamiMod.log.info("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        DiscordPresence.rpc.Discord_Initialize(ClientMain.APP_ID, handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;

        /* update rpc normally */
        setRpcFromSettings();

        /* update rpc while thread isn't interrupted  */
        new Thread(DiscordPresence::setRpcFromSettingsNonInt, "Discord-RPC-Callback-Handler").start();
//        KamiMod.log.info("Discord RPC initialised successfully");
    }

    private static void setRpcFromSettingsNonInt() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DiscordPresence.rpc.Discord_RunCallbacks();
//                discordSettings = ((DiscordSettings) ModuleManager.getModuleByName("DiscordRPC"));
                String separator = " | ";
//                details = discordSettings.getLine(discordSettings.line1Setting.getValue()) + separator + discordSettings.getLine(discordSettings.line3Setting.getValue());
//                state = discordSettings.getLine(discordSettings.line2Setting.getValue()) + separator + discordSettings.getLine(discordSettings.line4Setting.getValue());
                DiscordPresence.presence.details = details;
                DiscordPresence.presence.state = state;
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
            }
            catch (Exception e2) { e2.printStackTrace(); }
            try { Thread.sleep(4000L); }
            catch (InterruptedException e3) { e3.printStackTrace(); }
        }
    }
    private static void setRpcFromSettings() {
        details = "'Fun guys?'";
        state = "That's not even a homophone.";
//        state = Wrapper.getPlayer().dimension.toString();
//        discordSettings = ((DiscordSettings) ModuleManager.getModuleByName("DiscordRPC"));
//        details = discordSettings.getLine(discordSettings.line1Setting.getValue()) + " " + discordSettings.getLine(discordSettings.line3Setting.getValue());
//        state = discordSettings.getLine(discordSettings.line2Setting.getValue()) + " " + discordSettings.getLine(discordSettings.line4Setting.getValue());
        DiscordPresence.presence.details = details;
        DiscordPresence.presence.state = state;
//        DiscordPresence.presence.largeImageKey = "dmt";
        DiscordPresence.presence.largeImageText = "psilocybin cubensis";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
    }

    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
        DiscordPresence.hasStarted = false;
    }
}
