package com.git.toastclient.module.modules;

import com.git.toastclient.module.Module;

public class HighJump extends Module {

    public float velocity = 0;

    public HighJump(){
        MODULE_NAME = "HighJump";
    }

    public float jumpVelocity(){
        if(Toggled){
            return velocity;
        }

        return 0;
    }
}
