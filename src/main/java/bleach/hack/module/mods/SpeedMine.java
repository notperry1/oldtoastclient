package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.gui.clickgui.SettingMode;
import bleach.hack.gui.clickgui.SettingSlider;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class SpeedMine extends Module {

	public SpeedMine() {
		super("SpeedMine", -1, Category.EXPLOITS, "Allows you to mine at sanic speeds",
				new SettingMode("Mode: ", "Haste", "OG"),
				new SettingSlider("Haste Lvl: ", 1, 3, 1, 0),
				new SettingSlider("Cooldown: ", 0, 4, 1, 0),
				new SettingSlider("Multiplier: ", 1, 3, 1.3, 1));
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.player.removeStatusEffect(StatusEffects.HASTE);
	}

	@Subscribe
	public void onTick(EventTick event) {
		if (this.getSettings().get(0).toMode().mode == 0) {
			mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1, (int) getSettings().get(1).toSlider().getValue()));
		}
	}
}
