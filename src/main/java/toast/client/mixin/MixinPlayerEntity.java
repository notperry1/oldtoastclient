package toast.client.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toast.client.module.ModuleManager;
import toast.client.module.mods.movement.Safewalk;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    /*@Inject(at = @At("RETURN"), method = "clipAtLedge", cancellable = true)
    protected void onClipAtLedge(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(ModuleManager.getModule(Safewalk.class).isToggled());
    }*/
}
