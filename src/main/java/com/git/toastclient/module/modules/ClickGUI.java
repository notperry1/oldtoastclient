package com.git.toastclient.module.modules;

import com.git.toastclient.gui.ScreenMain;
import com.git.toastclient.module.Module;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import org.lwjgl.glfw.GLFW;

public class ClickGUI extends Module {

    private boolean buttonDown = false;
    private boolean wasButtonDown = false;

    public ClickGUI() {
        MODULE_NAME = "ClickGUI";
        initKeybind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void process() {
        if(Toggled){
            //Toggle ClickGUI once, Idk if theres a better way....
            ClientTickCallback.EVENT.register(e -> {
                buttonDown = Toggled;
                if (buttonDown && !wasButtonDown) {
                    Toggled = !Toggled;
                    mc().openScreen(new ScreenMain(new com.git.toastclient.gui.ClickGUI()));
                }
                wasButtonDown = buttonDown;
            });
        }
    }
}
