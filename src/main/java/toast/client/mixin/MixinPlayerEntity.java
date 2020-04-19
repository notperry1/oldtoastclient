package toast.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import toast.client.module.ModuleManager;
import toast.client.module.mods.hidden.Teleport;
import toast.client.module.mods.world.Scaffold;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    @Inject(at = @At("HEAD"), method = "jump()V", cancellable = true)
    public void jump (CallbackInfo info) {
        if (ModuleManager.getModule(Teleport.class).isToggled() || (ModuleManager.getModule(Scaffold.class).isToggled() && ((Scaffold) ModuleManager.getModule(Scaffold.class)).getTowering() && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getY() <= 255)) {
            info.cancel();
        }
    }
}
