package com.git.toastclient.module.modules;

import com.git.toastclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class Safewalk extends Module {

    @Override
    public void process(){
        if(Toggled){
            System.out.println("Test");
        }
    }

    public Safewalk(){
        initKeybind("Safewalk", GLFW.GLFW_KEY_V);
    }
}
