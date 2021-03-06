package toast.client.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import toast.client.ToastClient;
import toast.client.event.events.EventDrawOverlay;

@Mixin(InGameHud.class)
public class MixinIngameHud {
	
	@Inject(at = @At(value = "RETURN"), method = "render(F)V", cancellable = true)
	public void render(float float_1, CallbackInfo info) {
		EventDrawOverlay event = new EventDrawOverlay();
		ToastClient.eventBus.post(event);
		if (event.isCancelled()) info.cancel();
	}
}
