package bleach.hack.module.mods;

import com.google.common.eventbus.Subscribe;

import bleach.hack.event.events.EventEntityRender;
import bleach.hack.gui.clickgui.SettingMode;
import bleach.hack.gui.clickgui.SettingSlider;
import bleach.hack.gui.clickgui.SettingToggle;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.utils.EntityUtils;
import bleach.hack.utils.RenderUtilsLiving;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class Nametags extends Module {
	
	public Nametags() {
		super("Nametags", -1, Category.RENDER, "BROKEN!!! BROKEN!!! BROKEN!!! BROKEN!!! Shows bigger/cooler nametags above entities.",
				new SettingMode("Armor: ", "H", "V", "None"),
				new SettingMode("Health: ", "Number", "Bar"),
				new SettingSlider("Size Players: ", 0.5, 5, 2, 1),
				new SettingSlider("Size Mobs: ", 0.5, 5, 1, 1),
				new SettingToggle("Players", true),
				new SettingToggle("Mobs", false));
	}
	
	@Subscribe
	public void onLivingLabelRender(EventEntityRender.Label event) {
		if(event.getEntity() instanceof LivingEntity) event.setCancelled(true);
	}
	
	@Subscribe
	public void onLivingRender(EventEntityRender.Render event) {
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		LivingEntity e = (LivingEntity) event.getEntity();
		
		/* Color before name */
		String color = e instanceof Monster ? "§5" : EntityUtils.isAnimal(e)
				? "§a" : e.isSneaking() ? "§6" : e instanceof PlayerEntity ? "§c" : "§f";
		
		if(e == mc.player || e == mc.player.getVehicle() || color == "§f" || 
				((color == "§c" || color == "§6") && !getSettings().get(4).toToggle().state) ||
				((color == "§5" || color == "§a") && !getSettings().get(5).toToggle().state)) return;
		if(e.isInvisible()) color = "§e";
		
		double scale = (e instanceof PlayerEntity) ?
				Math.max(getSettings().get(2).toSlider().getValue() * (mc.cameraEntity.distanceTo(e) / 20), 1):
				Math.max(getSettings().get(3).toSlider().getValue() * (mc.cameraEntity.distanceTo(e) / 20), 1);
		
		/* Health bar */
		String health = "";
		/* - Add Green Normal Health */
		for(int i = 0; i < e.getHealth(); i++) health += "§a|";
		/* - Add Red Empty Health (Remove Based on absorption amount) */
		for(int i = 0; i < MathHelper.clamp(e.getAbsorptionAmount(), 0, e.getMaximumHealth() - e.getHealth()); i++) health += "§e|";
		/* Add Yellow Absorption Health */
		for(int i = 0; i < e.getMaximumHealth() - (e.getHealth() + e.getAbsorptionAmount()); i++) health += "§c|";
		/* Add "+??" to the end if the entity has extra hearts */
		if(e.getAbsorptionAmount() - (e.getMaximumHealth() - e.getHealth()) > 0) {
			health += " §e+" + (int)(e.getAbsorptionAmount() - (e.getMaximumHealth() - e.getHealth()));
		}
		
		/* Drawing Nametags */
		if(getSettings().get(1).toMode().mode == 0) {
			RenderUtilsLiving.drawText(color + e.getName().getString() + " [" + (int) (e.getHealth() + e.getAbsorptionAmount()) + "/" + (int) e.getMaximumHealth() + "]",
					e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + (0.5f * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), scale);
		}else if(getSettings().get(1).toMode().mode == 1) {
			RenderUtilsLiving.drawText(color + e.getName().getString(),
					e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + (0.5f * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), scale);
			RenderUtilsLiving.drawText(health,
					e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + (0.75f * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), scale);
		}
		
		/* Drawing Items */
		double c = 0;
		double higher = getSettings().get(1).toMode().mode == 1 ? 0.25 : 0;
		
		if(getSettings().get(0).toMode().mode == 0) {
			RenderUtilsLiving.drawItem(e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + ((0.75 + higher) * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), -2.5, 0, scale, e.getEquippedStack(EquipmentSlot.MAINHAND));
			RenderUtilsLiving.drawItem(e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + ((0.75 + higher) * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), 2.5, 0, scale, e.getEquippedStack(EquipmentSlot.OFFHAND));
			
			for(ItemStack i: e.getArmorItems()) {
				RenderUtilsLiving.drawItem(e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
						(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + ((0.75 + higher) * scale),
						e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), c+1.5, 0, scale, i);
				c--;
			}
		}else if(getSettings().get(0).toMode().mode == 1) {
			RenderUtilsLiving.drawItem(e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + ((0.75 + higher) * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), -1.25, 0, scale, e.getEquippedStack(EquipmentSlot.MAINHAND));
			RenderUtilsLiving.drawItem(e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
					(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + ((0.75 + higher) * scale),
					e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), 1.25, 0, scale, e.getEquippedStack(EquipmentSlot.OFFHAND));
			
			for(ItemStack i: e.getArmorItems()) {
				if(i.getCount() < 1) continue;
				RenderUtilsLiving.drawItem(e.prevX + (e.getX() - e.prevX) * mc.getTickDelta(),
						(e.prevY + (e.getY() - e.prevY) * mc.getTickDelta()) + e.getHeight() + ((0.75 + higher) * scale),
						e.prevZ + (e.getZ() - e.prevZ) * mc.getTickDelta(), 0, c, scale, i);
				c++;
			}
		}
		
		event.setCancelled(true);
	}
}
