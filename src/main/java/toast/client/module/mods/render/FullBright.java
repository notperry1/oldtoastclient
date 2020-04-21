package toast.client.module.mods.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import toast.client.event.events.EventTick;
import toast.client.gui.clickgui.SettingMode;
import toast.client.module.Category;
import toast.client.module.Module;

public class FullBright extends Module {

    public FullBright() {
        super("Fullbright", -1, Category.RENDER, "Turns your gamma setting up.",
                new SettingMode("Mode: ", "Gamma", "Gamma", "Potion"));
    }

    @Subscribe
    public void onTick(EventTick event) {
        if(getSettings().get(0).toMode().mode == 0) {
            if(mc.options.gamma < 16) mc.options.gamma += 1.2;

        } else if(getSettings().get(0).toMode().mode == 1) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1, 4));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.options.gamma = 1;
        mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }

}
