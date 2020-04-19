package toast.client.module.mods.world;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Blocks;
import toast.client.event.events.EventTick;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;

import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.WorldUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
	
	private HashMap<BlockPos, Integer> lastPlaced = new HashMap<>();
	public static boolean towering;
	
	public Scaffold() {
		super("Scaffold", GLFW.GLFW_KEY_N, Category.WORLD, "Places blocks under you",
				new SettingSlider("Range: ", 0.3, 1, 0.3, 1),
				new SettingMode("Mode: ", "Normal", "3x3", "5x5"),
				new SettingToggle("Tower: ", true),
				new SettingSlider("Tower Blocks: ", 0, 1, 0.3, 3));
	}

	@Subscribe
	public void onTick(EventTick event) {
		for(Entry<BlockPos, Integer> e: new HashMap<>(lastPlaced).entrySet()) {
			if(e.getValue() > 0) lastPlaced.replace(e.getKey(), e.getValue() - 1);
			else lastPlaced.remove(e.getKey());
		}
		
		int slot = -1;
		int prevSlot = mc.player.inventory.selectedSlot;
		
		if(mc.player.inventory.getMainHandStack().getItem() instanceof BlockItem) {
			slot = mc.player.inventory.selectedSlot;
		}else for(int i = 0; i < 9; i++) {
			if(mc.player.inventory.getInvStack(i).getItem() instanceof BlockItem) {
				slot = i;
				break;
			}
		}
		
		if(slot == -1) return;
		
		mc.player.inventory.selectedSlot = slot;
		double range = getSettings().get(0).toSlider().getValue();
		int mode = getSettings().get(1).toMode().mode;
		
		if(mode == 0) {
			for(int r = 0; r < 5; r++) {
				Vec3d r1 = new Vec3d(0,-0.85,0);
				if(r == 1) r1 = r1.add(range, 0, 0);
				if(r == 2) r1 = r1.add(-range, 0, 0);
				if(r == 3) r1 = r1.add(0, 0, range);
				if(r == 4) r1 = r1.add(0, 0, -range);
				
				if(placeBlockAuto(new BlockPos(mc.player.getPos().add(r1)))) {
					return;
				}
			}
		}else {
			int cap = 0;
			for(int x = (mode == 1 ? -1 : -2); x <= (mode == 1 ? 1 : 2); x++) {
				for(int z = (mode == 1 ? -1 : -2); z <= (mode == 1 ? 1 : 2); z++) {
					if(placeBlockAuto(new BlockPos(mc.player.getPos().add(x, -0.85, z)))) cap++;
					if(cap > 3) return;
				}
			}
		}
		
		mc.player.inventory.selectedSlot = prevSlot;

		if (mc.player.inventory.getMainHandStack().getItem() instanceof BlockItem && getSettings().get(2).toToggle().state &&
				mc.options.keyJump.isPressed() &&
				!WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(new BlockPos(mc.player.getBlockPos().getX(), mc.player.getBlockPos().getY() - 1d, mc.player.getBlockPos().getZ())).getBlock()) && mc.player.getY() <= 255) {
			towering = true;
			mc.player.setVelocity(0d, 0d, 0d);
			mc.player.updatePosition(mc.player.getX(), mc.player.getY() + getSettings().get(3).toSlider().getValue(), mc.player.getZ());
		}
		else {
			towering = false;
		}

	}
	
	public boolean placeBlockAuto(BlockPos block) {
		if(lastPlaced.containsKey(block) || !WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(block).getBlock())) {
			return false;
		}
		
		for(Direction d: Direction.values()) {
			if(!WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
				if(WorldUtils.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
				
				}
				mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, 
						new BlockHitResult(new Vec3d(block), d.getOpposite(), block.offset(d), false));
				mc.player.swingHand(Hand.MAIN_HAND);
				if(WorldUtils.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
				}
				lastPlaced.put(block, 5);
				return true;
			}
		}
		
		return false;
	}

	public boolean getTowering() {
		return towering;
	}

	public void setTowering(boolean bool) {
		towering = bool;
	}

}
