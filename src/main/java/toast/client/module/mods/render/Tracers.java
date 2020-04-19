package toast.client.module.mods.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import toast.client.event.events.Event3DRender;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.EntityUtils;
import toast.client.utils.RenderUtils;

import java.util.Arrays;
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
                new SettingToggle("Invisibles", true)); // 5
    }

    @Subscribe
    public void onRender(Event3DRender event) {
        if (mc.player == null || mc.world == null) return;
        List<Entity> entities = new java.util.ArrayList<>(Collections.emptyList());
        for (Entity e : mc.world.getEntities()) {
            entities.add(e);
        }
        entities.stream()
                .filter(EntityUtils::notSelf)
                .filter(entity -> (entity.isInvisible() && getSettings().get(5).toToggle().state) || !entity.isInvisible())
                .forEach(entity -> {
                    if (getSettings().get(0).toToggle().state && entity instanceof PlayerEntity) RenderUtils.drawLine(entity.getX(), entity.getY(), entity.getZ(), mc.player.getX(), mc.player.getY(), mc.player.getZ(), 1f, 1f, 0f, 1f);
                    else if (getSettings().get(1).toToggle().state && entity instanceof EnderCrystalEntity) RenderUtils.drawLine(entity.getX(), entity.getY(), entity.getZ(), mc.player.getX(), mc.player.getY(), mc.player.getZ(), 1f, 0f, 1f, 1f);
                    else if (getSettings().get(2).toToggle().state && EntityUtils.isAnimal(entity)) RenderUtils.drawLine(entity.getX(), entity.getY(), entity.getZ(), mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0f, 1f, 0f, 1f);
                    else if (getSettings().get(3).toToggle().state && EntityUtils.isNeutral(entity)) RenderUtils.drawLine(entity.getX(), entity.getY(), entity.getZ(), mc.player.getX(), mc.player.getY(), mc.player.getZ(),1f, 1f, 1f, 1f);
                    else if (getSettings().get(4).toToggle().state && EntityUtils.isHostile(entity)) RenderUtils.drawLine(entity.getX(), entity.getY(), entity.getZ(), mc.player.getX(), mc.player.getY(), mc.player.getZ(),1f, 0f, 0f, 1f);
                });
    }
}
