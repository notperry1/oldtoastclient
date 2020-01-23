package com.git.toastclient.mixin;

import com.git.toastclient.module.Module;
import com.git.toastclient.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class TickMixin {

        @Inject(method = ("tick"),at = @At("TAIL"))
        public void onTick(CallbackInfo callbackInfo) {
            if(MinecraftClient.getInstance().world != null){
                for(Module m: ModuleManager.Modules.values()) {
                    m.process();
                }
            }
        }
}
