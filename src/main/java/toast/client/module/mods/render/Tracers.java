package toast.client.module.mods.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.util.math.Vec3d;
import toast.client.event.events.Event3DRender;
import toast.client.event.events.EventReadPacket;
import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.utils.EntityUtils;
import toast.client.utils.RenderUtils;
import toast.client.utils.RenderUtilsLiving;
import toast.client.utils.file.ToastFileHelper;

import java.util.*;

public class Tracers extends Module {

    private static LinkedHashMap<String, Vec3d> waypoints = new LinkedHashMap<>();

    public Tracers() {
        super("Tracers", -1, Category.RENDER, "Draws lines to entities",
                new SettingToggle("Players", true), // 0
                new SettingSlider("P Range: ", 1, 200, 200, 0), // 1
                new SettingToggle("Crystals", true), // 2
                new SettingSlider("C Range", 1, 200, 20, 0), // 3
                new SettingToggle("Passive", false), // 4
                new SettingToggle("Neutral", false), // 5
                new SettingToggle("Hostile", false), // 6
                new SettingSlider("M Range: ", 1, 200, 30, 0), // 7
                new SettingToggle("Invisibles", true), // 8
                new SettingSlider("Thickness: ", 1, 10, 1.5, 2), // 9
                new SettingMode("Trace To: ", "Body", "Feet", "Body", "Eyes"), // 10
                new SettingToggle("Waypoints", true)); // 11
    }

    @Subscribe
    public void readPacket(EventReadPacket event) {
        if (event.getPacket() instanceof LoginHelloC2SPacket) {
            ToastFileHelper.readWaypoints();
        }
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
                .filter(entity ->
                        (entity.isInvisible() &&
                                getSettings().get(8).toToggle().state) ||
                                !entity.isInvisible())
                .filter(entity ->
                        !(entity instanceof PlayerEntity &&
                                entity.getPos().distanceTo(mc.player.getPos()) > Objects.requireNonNull(ModuleManager.getModule(Tracers.class)).getSettings().get(1).toSlider().getValue()))
                .filter(entity ->
                        !(entity instanceof EnderCrystalEntity &&
                                entity.getPos().distanceTo(mc.player.getPos()) > Objects.requireNonNull(ModuleManager.getModule(Tracers.class)).getSettings().get(3).toSlider().getValue()))
                .filter(entity ->
                        !(
                                (EntityUtils.isAnimal(entity)
                                || EntityUtils.isNeutral(entity)
                                || EntityUtils.isHostile(entity)) &&
                                entity.getPos().distanceTo(mc.player.getPos()) > Objects.requireNonNull(ModuleManager.getModule(Tracers.class)).getSettings().get(7).toSlider().getValue()))
                .filter(entity ->
                        (getSettings().get(0).toToggle().state && entity instanceof PlayerEntity) ||
                        (getSettings().get(2).toToggle().state && entity instanceof EnderCrystalEntity) ||
                        (getSettings().get(4).toToggle().state && EntityUtils.isAnimal(entity)) ||
                        (getSettings().get(5).toToggle().state && EntityUtils.isNeutral(entity)) ||
                        (getSettings().get(6).toToggle().state && EntityUtils.isHostile(entity)))
                .forEach(entity -> {
                    RenderUtils.drawLineFromEntity(entity, getSettings().get(10).toMode().mode, camera.getX(), camera.getY(), camera.getZ(), (float) getSettings().get(9).toSlider().getValue());
                });

        if (getSettings().get(11).toToggle().state) {
            waypoints.entrySet()
                    .forEach(waypoint -> {
                                RenderUtils.drawLine(waypoint.getValue().x, waypoint.getValue().y, waypoint.getValue().z, camera.getX(), camera.getY(), camera.getZ(), 0.66f, 0.56f, 0.79f, (float) getSettings().get(9).toSlider().getValue());
                                RenderUtils.drawLine(waypoint.getValue().x, 0, waypoint.getValue().z, waypoint.getValue().x, 256, waypoint.getValue().z, 0.66f, 0.56f, 0.79f, (float) getSettings().get(9).toSlider().getValue());
                                RenderUtilsLiving.drawText(waypoint.getKey() + " (" + waypoint.getValue().x + ", " + waypoint.getValue().y + ", " + waypoint.getValue().z + ")", waypoint.getValue().x, waypoint.getValue().y + 2, waypoint.getValue().z, 1 + waypoint.getValue().distanceTo(mc.player.getPos()) / 12.5);
                            });
        }
    }

    public static LinkedHashMap<String, Vec3d> getWaypoints() {
        return waypoints;
    }

    public static void addWaypoint(String name, Vec3d position) {
        waypoints.put(name, position);
        ToastFileHelper.saveWaypoints();
    }

    public static void removeWaypoint(String name) {
        waypoints.remove(name);
        ToastFileHelper.saveWaypoints();
    }

    public static Vec3d stringToVec3d(String string) {
        String[] stringSet = string.substring(1).substring(0, string.length() - 1).split(", ");
        return new Vec3d(Double.parseDouble(stringSet[0]), Double.parseDouble(stringSet[1]), Double.parseDouble(stringSet[2]));
    }
}
