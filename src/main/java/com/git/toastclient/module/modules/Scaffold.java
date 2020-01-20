package com.git.toastclient.module.modules;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;

import com.git.toastclient.Helper;
import com.git.toastclient.module.Module;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.EntityAnimationS2CPacket;
import net.minecraft.client.network.packet.HeldItemChangeS2CPacket;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket.Flag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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

	Set<Flag> flags = new HashSet<Flag>(); {
		flags.add(Flag.X_ROT);
		flags.add(Flag.Y_ROT);
	}
	
	boolean place() {
		boolean success = false;
		
		final Vec3d eyes = mc().player.getPos().add(0.0d, mc().player.getEyeHeight(mc().player.getPose()), 0.0d);
		BlockPos floor, neighbour;
		
		System.out.printf("My eyes are at %s\n", Helper.coords(eyes));
		for (final Direction side : directions) {
			floor = new BlockPos(mc().player).down();
			neighbour = floor.offset(side);
			
			final Direction opposite = side.getOpposite();
			
			final Vec3d hitVec = new Vec3d(neighbour).add(new Vec3d(opposite.getUnitVector()).multiply(0.5d));

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
			
//			float oldYaw = mc().player.yaw;
//			float oldPitch = mc().player.pitch;
			{	// look at block
				double dx = hitVec.x - eyes.x;
				double dy = hitVec.y - eyes.y;
				double dz = hitVec.z - eyes.z;
				double dxz = MathHelper.sqrt(dx * dx + dz * dz);
				
				float yaw = MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
				float pitch = MathHelper.wrapDegrees((float)-Math.toDegrees(Math.atan2(dy, dxz)));
				
//				mc().player.networkHandler.sendPacket(
//					new PlayerPositionLookS2CPacket(
//						mc().player.getX(), mc().player.getY(), mc().player.getZ(),
//						yaw, pitch,
//						flags, (int)(Math.random()*1000)
//					)
//				);
			}
			
			{// place block
				mc().interactionManager.interactBlock(mc().player,
						mc().world, Hand.MAIN_HAND,
						new BlockHitResult(hitVec, side, neighbour.offset(opposite), true));
				System.out.printf("Block placed at %s against the %s neighbor at %s on the %s side with hitvec %s\n",
						Helper.coords(floor),
						side.getName(),
						Helper.coords(neighbour),
						opposite.getName(),
						Helper.coords(hitVec)
					);
				success = true;
			}
			
			{// swing arm
				mc().player.swingHand(Hand.MAIN_HAND);
//				mc().player.networkHandler.sendPacket(
//					new EntityAnimationS2CPacket(mc().player, 1)
//				);
			}
			
			/*{// look back

				mc().player.networkHandler.sendPacket(
					new PlayerPositionLookS2CPacket(
						mc().player.getX(), mc().player.getY(), mc().player.getZ(),
						oldYaw, oldPitch,
						null, 0
					)
				);
			}*/
		}
		return success;
	}

	@Override
	public void process() {
		if (Toggled) {
			if (check()) {
				final int oldSlot = mc().player.inventory.selectedSlot;
				final int getSlot = findSlot();
				mc().player.networkHandler.sendPacket(
					new HeldItemChangeS2CPacket(getSlot)
				);
				if (!place()) {
					
				}
				mc().player.networkHandler.sendPacket(
					new HeldItemChangeS2CPacket(oldSlot)
				);
			}
		}
	}
}
