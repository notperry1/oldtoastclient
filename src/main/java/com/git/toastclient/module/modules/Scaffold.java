package com.git.toastclient.module.modules;

import com.git.toastclient.module.Module;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.packet.HandSwingC2SPacket;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Scaffold extends Module {

	public Scaffold() {
		MODULE_NAME = "Scaffold";
		initKeybind(GLFW.GLFW_KEY_C);
	}

	boolean check() {
		final BlockPos floor = mc().player.getBlockPos().down();
		final BlockState floorBlock = mc().world.getBlockState(floor);

		return floorBlock.isAir() || (floorBlock.getBlock() instanceof FluidBlock);
	}

	int findSlot() {
		int blockSlot = -1;

		for (int i = 0; i < 9; i++) {
			final ItemStack stack = mc().player.inventory.getInvStack(i);
			if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
				continue;
			}

			final Block b = Block.getBlockFromItem(stack.getItem());
			if (!b.getDefaultState().isOpaque()) {
				if (!(b instanceof GlassBlock || b instanceof StainedGlassBlock)) {
					continue;
				}
			}
			if (b instanceof FallingBlock) {
				continue;
			}
			blockSlot = i;
			break;
		}
		
		return blockSlot;
	}

	Direction directions[] = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	
	boolean place() {
		boolean success = false;
		
		final Vec3d eyes = mc().player.getPos().add(0.0d, mc().player.getEyeHeight(mc().player.getPose()), 0.0d);
		BlockPos floor, neighbour;
		float oldYaw = mc().player.yaw;
		float oldPitch = mc().player.pitch;
		
		for (final Direction side : directions) {
			floor = new BlockPos(mc().player).down();
			neighbour = floor.offset(side);
			
			final Direction opposite = side.getOpposite();
			
			Vec3d hitVec = new Vec3d(neighbour)
					.add(new Vec3d(opposite.getUnitVector()).multiply(0.5d))
					.add(new Vec3d(0.5, 0.5, 0.5));
			
//			if (eyes.squaredDistanceTo(hitVec) > Math.pow(mc().interactionManager.getReachDistance(), 2)) {
//				System.out.printf("[SHOULDNTHAPPEN] too far away, cannot reach\n");
//				continue;
//			}

			if (!mc().player.canPlaceOn(floor, side, mc().player.getStackInHand(Hand.MAIN_HAND))) {
//				System.out.printf("cannot place on %s neighbor at %s with %s\n", side.name(), Helper.coords(neighbour));
				continue;
			}

			final BlockState neighbourblockstate = mc().world.getBlockState(neighbour);
//			final Block neighbourblock = neighbourblockstate.getBlock();
			if (neighbourblockstate.isAir()) {
//				System.out.printf("%s neighbor at %s is air\n", side.name(), Helper.coords(neighbour));
				continue;
			}
//			if (neighbourblock instanceof FluidBlock) {
//				System.out.printf("%s neighbor at %s is fluid\n", side.name(), Helper.coords(neighbour));
//				continue;
//			}
			
			
			{// look at block
				double dx = hitVec.x - eyes.x;
				double dy = hitVec.y - eyes.y;
				double dz = hitVec.z - eyes.z;
				double dxz = MathHelper.sqrt(dx * dx + dz * dz);
				
				float yaw   = (float) MathHelper.wrapDegrees( Math.toDegrees(Math.atan2(dz,  dx)) - 90.0F);
				float pitch = (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(dy, dxz))        );
//				System.out.printf("dx:%4f dy:%4f dz:%4f yaw:%1f pitch:%1f\n", dx, dy, dz, yaw, pitch);
				
				mc().player.networkHandler.sendPacket(
					new PlayerMoveC2SPacket.LookOnly(
						yaw,
						pitch,
						mc().player.onGround
					)
				);
			}
			
			{// place block
				mc().interactionManager.interactBlock(
					mc().player,
					mc().world,
					Hand.MAIN_HAND,
					new BlockHitResult(hitVec, side, neighbour.offset(opposite), true)
				);
//				System.out.printf("Block placed at %s against the %s neighbor at %s on the %s side with hitvec %s\n",
//					Helper.coords(floor),
//					side.getName(),
//					Helper.coords(neighbour),
//					opposite.getName(),
//					Helper.coords(hitVec)
//				);
				success = true;
				break;
			}
		}
		if (success) {
			{// swing arm
//				mc().player.swingHand(Hand.MAIN_HAND);
				mc().player.networkHandler.sendPacket(
					new HandSwingC2SPacket(Hand.MAIN_HAND)
				);
			}
			
			{// look back
				mc().player.networkHandler.sendPacket(
					new PlayerMoveC2SPacket.LookOnly(
						oldYaw,
						oldPitch,
						mc().player.onGround
					)
				);
				mc().player.yaw = oldYaw;
				mc().player.pitch = oldPitch;
				mc().player.prevYaw = mc().player.yaw;
				mc().player.prevPitch = mc().player.pitch;
				mc().player.setHeadYaw(mc().player.yaw);
				mc().player.setYaw(mc().player.yaw);
			}
		}
		
		return success;
	}

	@Override
	public void process() {
		if (Toggled) {
			if (check()) {
				final int oldSlot = mc().player.inventory.selectedSlot;
				final int getSlot = findSlot();
				if (getSlot == -1) {
//					enable safewalk!
					return;
				}
				mc().player.inventory.selectedSlot = getSlot;
				if (!place()) {
//					enable safewalk!
				}
				mc().player.inventory.selectedSlot = oldSlot;
			}
		}
	}
}
