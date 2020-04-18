package toast.client.module.mods;

import toast.client.event.events.EventSendPacket;
import toast.client.event.events.EventTick;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.ToastLogger;

import com.google.common.eventbus.Subscribe;

import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.util.math.Vec3d;

/**
 * @author Cosmic (pro skidder)
 */
public class Teleport extends Module {

	private long lastTp;
    private Vec3d lastPos;
    public static Vec3d finalPos;

    public Teleport() {
        super("Teleport", -1, Category.HIDDEN, "What are you doing here?",
            new SettingSlider("BPT: ", 0.01, 20000, 1, 2));
    }

    @Subscribe
    public void sendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof LoginHelloC2SPacket) {
            setToggled(false);
        }
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) return;
    	if(finalPos == null) {
    		ToastLogger.errorMessage("Position not set, use .tp");
    		setToggled(false);
    		return;
    	}

        Vec3d tpDirectionVec = finalPos.subtract(mc.player.getPosVector()).normalize();

        int chunkX = (int) Math.floor(mc.player.getPosVector().x / 16.0D);
        int chunkZ = (int) Math.floor(mc.player.getPosVector().z / 16.0D);
        if (mc.world.isChunkLoaded(chunkX, chunkZ)) {
            lastPos = mc.player.getPosVector();
            if (finalPos.distanceTo(mc.player.getPosVector()) < 0.3 || getSettings().get(0).toSlider().getValue() == 0) {
                ToastLogger.infoMessage("Teleport Finished!");
                setToggled(false);
            } else {
                mc.player.setVelocity(0,0,0);
            }

            if (finalPos.distanceTo(mc.player.getPosVector()) >= getSettings().get(0).toSlider().getValue()) {
                final Vec3d vec = tpDirectionVec.multiply(getSettings().get(0).toSlider().getValue());
                mc.player.setPos(mc.player.getPos().getX() + vec.getX(), mc.player.getPos().getY() + vec.getY(), mc.player.getPos().getZ() + vec.getZ());
            } else {
                final Vec3d vec = tpDirectionVec.multiply(finalPos.distanceTo(mc.player.getPosVector()));
                mc.player.setPos(mc.player.getPosVector().getX() + vec.x, mc.player.getPosVector().getY() + vec.y, mc.player.getPosVector().getZ() + vec.z);
            }
            lastTp = System.currentTimeMillis();
        } else if (lastTp + 2000L > System.currentTimeMillis()) {
            mc.player.setPos(lastPos.x, lastPos.y, lastPos.z);
        }
    }

}
