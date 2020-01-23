package com.git.toastclient.mixin;

import com.git.toastclient.ClientMain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.entity.player.PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = ("clipAtLedge"), at = @At("RETURN"), cancellable = true)
    protected void onClipAtLedge(CallbackInfoReturnable<Boolean> info) {
    	info.setReturnValue(ClientMain.Modules.get("Safewalk").Toggled);
	}

}
