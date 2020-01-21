package com.git.toastclient.module.modules;

import com.git.toastclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class Safewalk extends Module {

    public Safewalk() {
    	MODULE_NAME = "Safewalk";
        initKeybind(GLFW.GLFW_KEY_V);
    }

    @Override
    public void process() {
        if(Toggled){
        }
    }
}
