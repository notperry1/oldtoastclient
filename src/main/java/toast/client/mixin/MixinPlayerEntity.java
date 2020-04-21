package toast.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import toast.client.module.ModuleManager;
import toast.client.module.mods.combat.Surround;
import toast.client.module.mods.hidden.Teleport;
import toast.client.module.mods.world.Scaffold;
import toast.client.utils.WorldUtils;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

    @Inject(at = @At("HEAD"), method = "jump()V", cancellable = true)
    public void jump (CallbackInfo info) {
        if (ModuleManager.getModule(Teleport.class).isToggled() ||
                ModuleManager.getModule(Surround.class).isToggled() ||
                (ModuleManager.getModule(Scaffold.class).isToggled() &&
                        ModuleManager.getModule(Scaffold.class).getSettings().get(2).toToggle().state &&
                        MinecraftClient.getInstance().player != null &&
                        MinecraftClient.getInstance().player.inventory.getMainHandStack().getItem() instanceof BlockItem &&
                        !WorldUtils.NONSOLID_BLOCKS.contains(MinecraftClient.getInstance().world.getBlockState(new BlockPos(MinecraftClient.getInstance().player.getBlockPos().getX(), MinecraftClient.getInstance().player.getBlockPos().getY() - 1d, MinecraftClient.getInstance().player.getBlockPos().getZ())).getBlock()) && MinecraftClient.getInstance().player.getY() <= 255)) {
            info.cancel();
        }
    }
}
