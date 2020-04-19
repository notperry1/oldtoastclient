package toast.client.module.mods.render;

import com.google.common.eventbus.Subscribe;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import toast.client.event.events.Event3DRender;
import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.EntityUtils;
import toast.client.utils.RenderUtils;

import java.util.Collections;
import java.util.List;

public class Tracers extends Module {

    public Tracers() {
        super("Tracers", -1, Category.RENDER, "Draws lines to entities",
                new SettingToggle("Players", true), // 0
                new SettingToggle("Crystals", true), // 1
                new SettingToggle("Passive", false), // 2
                new SettingToggle("Neutral", false), // 3
                new SettingToggle("Hostile", false), // 4
                new SettingToggle("Invisibles", true), // 5
                new SettingSlider("Thickness", 1, 10, 1.5, 2), // 6
                new SettingMode("Trace To: ", "Body", "Feet", "Body", "Eyes")); // 7
    }

    @Subscribe
    public void onRender(Event3DRender event) {
        if (mc.player == null || mc.world == null) return;
        List<Entity> entities = new java.util.ArrayList<>(Collections.emptyList());
        for (Entity entity : mc.world.getEntities()) {
            entities.add(entity);
        }
        Vec3d camera = new Vec3d(0, 0, 75).rotateX(-(float) Math.toRadians(mc.cameraEntity.pitch))
                .rotateY(-(float) Math.toRadians(mc.cameraEntity.yaw))
                .add(mc.cameraEntity.getPos().add(0, mc.cameraEntity.getEyeHeight(mc.cameraEntity.getPose()), 0));
        entities.stream()
                .filter(EntityUtils::notSelf)
                .filter(entity -> (entity.isInvisible() && getSettings().get(5).toToggle().state) || !entity.isInvisible())
                .filter(entity -> (getSettings().get(0).toToggle().state && entity instanceof PlayerEntity) ||
                        (getSettings().get(1).toToggle().state && entity instanceof EnderCrystalEntity) ||
                        (getSettings().get(2).toToggle().state && EntityUtils.isAnimal(entity)) ||
                        (getSettings().get(3).toToggle().state && EntityUtils.isNeutral(entity)) ||
                        (getSettings().get(0).toToggle().state && EntityUtils.isHostile(entity)))
                .forEach(entity -> {
                    RenderUtils.drawLineFromEntity(entity, getSettings().get(7).toMode().mode, camera.getX(), camera.getY(), camera.getZ(), (float) getSettings().get(6).toSlider().getValue());
                });
    }
}
