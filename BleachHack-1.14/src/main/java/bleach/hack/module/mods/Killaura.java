package bleach.hack.module.mods;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

import bleach.hack.gui.clickgui.SettingBase;
import bleach.hack.gui.clickgui.SettingSlider;
import bleach.hack.gui.clickgui.SettingToggle;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class Killaura extends Module {

	private static List<SettingBase> settings = Arrays.asList(
			new SettingToggle(true, "Players"),
			new SettingToggle(true, "Mobs"),
			new SettingToggle(false, "Animals"),
			new SettingToggle(false, "Armor Stands"),
			new SettingToggle(true, "Aimbot"),
			new SettingToggle(false, "Thru Walls"),
			new SettingToggle(false, "1.9 Delay"),
			new SettingSlider(0, 6, 4.25, 2, "Range: "),
			new SettingSlider(0, 20, 8, 0, "CPS: "));
	
	private int delay = 0;
	
	public Killaura() {
		super("Killaura", GLFW.GLFW_KEY_K, Category.COMBAT, "Automatically attacks entities", settings);
	}
	
	public void onUpdate() {
		if(this.isToggled()) {
			delay++;
			int reqDelay = (int) Math.round(20/getSettings().get(8).toSlider().getValue());
			if(getSettings().get(6).toToggle().state) reqDelay = (int)Math.ceil(mc.player.getCooldownPeriod());
			
			List<Entity> targets = EntityUtils.getLoadedEntities().stream()
					.filter(e -> (e instanceof PlayerEntity && getSettings().get(0).toToggle().state)
							|| (e instanceof IMob && getSettings().get(1).toToggle().state)
							|| (EntityUtils.isAnimal(e) && getSettings().get(2).toToggle().state)
							|| (e instanceof ArmorStandEntity && getSettings().get(3).toToggle().state))
					.collect(Collectors.toList());
			targets.sort((a,b) -> Float.compare(a.getDistance(mc.player), b.getDistance(mc.player)));
			
			for(Entity e: targets) {
				if(mc.player.getDistance(e) > getSettings().get(7).toSlider().getValue()
						|| !e.isAlive()
						|| e == mc.player || e == mc.player.getRidingEntity() || e == mc.renderViewEntity
						|| (!mc.player.canEntityBeSeen(e) && !getSettings().get(5).toToggle().state)) continue;
				
				if(getSettings().get(4).toToggle().state) EntityUtils.facePos(e.posX, e.posY+e.getHeight()/2, e.posZ);
					
				if(delay > reqDelay || reqDelay == 0) {
					mc.playerController.attackEntity(mc.player, e);
					mc.player.swingArm(Hand.MAIN_HAND);
					delay=0;
				}
			}
		}
	}

}
