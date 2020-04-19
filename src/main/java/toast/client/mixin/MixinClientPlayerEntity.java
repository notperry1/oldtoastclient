package toast.client.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toast.client.ToastClient;
import toast.client.event.events.EventMovementTick;
import toast.client.event.events.EventTick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import toast.client.module.ModuleManager;
import toast.client.module.mods.hidden.Teleport;
import toast.client.module.mods.world.Scaffold;
import toast.client.utils.ToastQueue;
import toast.client.utils.file.ToastFileHelper;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
	
	@Inject(at = @At("RETURN"), method = "tick()V", cancellable = true)
	public void tick(CallbackInfo info) {
		try {
			if(MinecraftClient.getInstance().player.age % 100 == 0) {
				ToastFileHelper.saveModules();
				ToastFileHelper.saveSettings();
				ToastFileHelper.saveBinds();
				ToastFileHelper.saveClickGui();
			}
			
			ToastQueue.nextQueue();
		}catch(Exception e) {}
		EventTick event = new EventTick();
		ToastClient.eventBus.post(event);
		if (event.isCancelled()) info.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "sendMovementPackets()V", cancellable = true)
	public void sendMovementPackets(CallbackInfo info) {
		EventMovementTick event = new EventMovementTick();
		ToastClient.eventBus.post(new EventMovementTick());
		if (event.isCancelled()) info.cancel();
	}

	@Inject(at = @At("HEAD"), method = "canMoveVoluntarily()Z", cancellable = true)
	public void canMoveVoluntarily(CallbackInfoReturnable<Boolean> info) {
		if ((ModuleManager.getModule(Scaffold.class).isToggled() && ((Scaffold) ModuleManager.getModule(Scaffold.class)).getTowering()) || ModuleManager.getModule(Teleport.class).isToggled()) {
			info.setReturnValue(false);
		}
	}

}

