package com.git.toastclient.module.modules;

import com.git.toastclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class InvWalk extends Module {

    public InvWalk() {
        MODULE_NAME = "InvWalk";
        initKeybind(GLFW.GLFW_KEY_B);
    }

    @Override
    public void process() {

    }
}
