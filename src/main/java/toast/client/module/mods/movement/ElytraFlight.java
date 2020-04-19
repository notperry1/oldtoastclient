package toast.client.module.mods.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import toast.client.event.events.EventTick;
import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.module.mods.world.Scaffold;
import toast.client.utils.ToastLogger;
import toast.client.utils.WorldUtils;

import java.util.Objects;

public class ElytraFlight extends Module {
    public ElytraFlight() {
        super("ElytraFlight", -1, Category.MOVEMENT, "Modifies elytras to fly at custom velocities and fall speeds",
                new SettingMode("Mode: ", "Highway", "Boost", "Fly", "Highway"), // 0
                new SettingToggle("Reset Settings", false), // 1
                new SettingToggle("Easy Takeoff", true), // 2
                new SettingMode("Takeoff Mode: ", "Packet", "Client", "Packet"), // 3
                new SettingToggle("Over Max Speed", false), // 4
                new SettingSlider("Speed H: ", 0d, 1.8d, 1.8d, 3), // 5
                new SettingSlider("Speed H O: ", 0d, 10d, 1.8d, 3), // 6
                new SettingSlider("Fall Speed H: ", 0d, 0.1d, 0.000050000002f, 15), // 7
                new SettingSlider("Fall Speed: ", -0.1d, 1d, -0.003d, 15), // 8
                new SettingSlider("Up Speed B: ", 0d, 0.1d, 0.08d, 3), // 9
                new SettingSlider("Down Speed B: ", 0d, 0.1d, 0.04d, 3)); // 10
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (mc.player == null || !isToggled()) {
            return;
        }
        
        if (getSettings().get(1).toToggle().state) {
            getSettings().get(2).toToggle().state = true;
            getSettings().get(3).toMode().mode = 1;
            getSettings().get(4).toToggle().state = false;
            getSettings().get(5).toSlider().setValue(1.8d);
            getSettings().get(6).toSlider().setValue(1.8d);
            getSettings().get(7).toSlider().setValue(0.000050000002f);
            getSettings().get(8).toSlider().setValue(-0.003d);
            getSettings().get(9).toSlider().setValue(0.08d);
            getSettings().get(10).toSlider().setValue(0.04d);
            ToastLogger.infoMessage("Reset ElytraFlight settings!");
        }

        takeOff();
        setFlySpeed();

        if (mc.player.onGround) mc.player.abilities.allowFlying = false;

        if (getSettings().get(0).toMode().mode == 0) {
            if (mc.player.isInsideWaterOrBubbleColumn()) {
                Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                return;
            }

            if (mc.options.keyJump.isPressed() && !(ModuleManager.getModule(Scaffold.class).isToggled() &&
                    ModuleManager.getModule(Scaffold.class).getSettings().get(2).toToggle().state &&
                    MinecraftClient.getInstance().player != null &&
                    MinecraftClient.getInstance().player.inventory.getMainHandStack().getItem() instanceof BlockItem &&
                    !WorldUtils.NONSOLID_BLOCKS.contains(MinecraftClient.getInstance().world.getBlockState(new BlockPos(MinecraftClient.getInstance().player.getBlockPos().getX(), MinecraftClient.getInstance().player.getBlockPos().getY() - 1d, MinecraftClient.getInstance().player.getBlockPos().getZ())).getBlock()) && MinecraftClient.getInstance().player.getY() <= 255)) {
                mc.player.addVelocity(0, getSettings().get(9).toSlider().getValue(), 0);
            }
            else if (mc.options.keySneak.isPressed()) {
                mc.player.addVelocity(0, -getSettings().get(10).toSlider().getValue(), 0);
            }

            if (mc.options.keyForward.isPressed()) {
                float yaw = (float) Math.toRadians(mc.player.yaw);
                mc.player.addVelocity(-MathHelper.sin(yaw) * 0.05F, 0, MathHelper.cos(yaw) * 0.05F);
            } else if (mc.options.keyBack.isPressed()) {
                float yaw = (float) Math.toRadians(mc.player.yaw);
                mc.player.addVelocity(MathHelper.sin(yaw) * 0.05F, 0, -MathHelper.cos(yaw) * 0.05F);
            }
        } else {
            mc.player.abilities.setFlySpeed(0.915f);
            mc.player.abilities.flying = true;

            if (mc.player.abilities.creativeMode)
                return;
            mc.player.abilities.allowFlying = true;
        }
    }

    public void onDisable() {
        if (mc.player == null) {
            return;
        }
        mc.player.abilities.flying = false;
        mc.player.abilities.setFlySpeed(0.05f);
        mc.player.abilities.allowFlying = mc.player.abilities.creativeMode;
    }

    private void setFlySpeed() {
        if (mc.player == null) {
            return;
        }
        
        if (mc.player.abilities.flying) {
            if (getSettings().get(0).toMode().mode == 2) {
                mc.player.setSprinting(false);
                mc.player.setVelocity(0, 0, 0);
                mc.player.updatePosition(mc.player.getX(), mc.player.getY() - getSettings().get(7).toSlider().getValue(), mc.player.getZ());
                mc.player.abilities.setFlySpeed(getHighwaySpeed());
            } else {
                mc.player.setVelocity(0, 0, 0);
                mc.player.abilities.setFlySpeed(.915f);
                mc.player.updatePosition(mc.player.getX(), mc.player.getY() - getSettings().get(8).toSlider().getValue(), mc.player.getY());
            }
        }
    }

    private void takeOff() {
        if (mc.player == null) {
            return;
        }
        
        if (!(getSettings().get(0).toMode().mode == 2 && getSettings().get(2).toToggle().state)) return;
        if (mc.player.isFallFlying() && !mc.player.onGround) {
            switch (getSettings().get(3).toMode().mode) {
                case 0:
                    mc.player.abilities.flying = true;
                case 1:
                    Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                default:
                    mc.player.abilities.flying = true;
            }
        }

        if (mc.player.isFallFlying()) {
            getSettings().get(2).toToggle().state = false;
            ToastLogger.warningMessage("Disabled takeoff!");
        }
    }

    private float getHighwaySpeed() {
        if (getSettings().get(4).toToggle().state) {
            return (float) getSettings().get(6).toSlider().getValue();
        } else {
            return (float) getSettings().get(5).toSlider().getValue();
        }
    }
    
}
