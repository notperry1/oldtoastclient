package com.git.toastclient.module.modules;

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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

	public Scaffold() {
		initKeybind("Scaffold", GLFW.GLFW_KEY_C);
	}

	boolean check() {
		final BlockPos floor = new BlockPos(MinecraftClient.getInstance().player).down();
		final BlockState floorBlock = MinecraftClient.getInstance().world.getBlockState(floor);

		return floorBlock.isAir();
	}

	boolean hold() {
		int blockSlot = -1;

		for (int i = 0; i < 9; i++) {
			final ItemStack stack = MinecraftClient.getInstance().player.inventory.getInvStack(i);
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

		if (blockSlot != -1) {
			MinecraftClient.getInstance().player.inventory.selectedSlot = blockSlot;
			return true;
		} else {
			return false;
		}
	}

	boolean place() {
		if (hold()) {
			final Vec3d eyes = MinecraftClient.getInstance().player.getPos().add(0.0d,
					MinecraftClient.getInstance().player.getEyeHeight(MinecraftClient.getInstance().player.getPose()),
					0.0d);
			// System.out.printf("My eyes are at %f %f %f\n", eyes.x, eyes.y, eyes.z);
			for (final Direction side : Direction.values()) {
				// System.out.printf("The floor is at %d %d %d\n", floor.getX(), floor.getY(),
				// floor.getZ());
				// System.out.printf("It's %s neighbor is at %d %d %d\n", side.getName(),
				// neighbour.getX(), neighbour.getY(), neighbour.getZ());
				final BlockPos floor = new BlockPos(MinecraftClient.getInstance().player).down();
				final BlockPos neighbour = floor.offset(side);
				final Direction opposite = side.getOpposite();
				final Vec3d hitVec = new Vec3d(neighbour).add(new Vec3d(opposite.getUnitVector()).multiply(0.5d));
				if (eyes.squaredDistanceTo(new Vec3d(floor)) <= eyes.squaredDistanceTo(new Vec3d(neighbour))) {
					System.out.printf("floor is closer than neighbor, we are fine");
					continue;
				}
				if (eyes.squaredDistanceTo(hitVec) > Math.pow(MinecraftClient.getInstance().interactionManager.getReachDistance(), 2)) {
					System.out.printf("too far away, cannot reach");
					continue;
				}
				if (!MinecraftClient.getInstance().player.canPlaceOn(floor, side,
						MinecraftClient.getInstance().player.getStackInHand(Hand.MAIN_HAND))) {
					System.out.printf("cannot place on %s neighbor at %s with %s\n",
							side.name(),
							Helper.coords(neighbour)
						);
					continue;
				}
				final BlockState neighbourblockstate = MinecraftClient.getInstance().world.getBlockState(neighbour);
				final Block neighbourblock = neighbourblockstate.getBlock();
				if (neighbourblockstate.isAir()) {
					System.out.printf("%s neighbor at %s is air\n", side.name(), Helper.coords(neighbour));
					continue;
				}
				if (neighbourblock instanceof FluidBlock) {
					System.out.printf("%s neighbor at %s is fluid\n", side.name(), Helper.coords(neighbour));
					continue;
				}
				{// place block
					MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player,
							MinecraftClient.getInstance().world, Hand.MAIN_HAND,
							new BlockHitResult(hitVec, side, neighbour.offset(opposite), true));
					System.out.printf("Block placed at %s against the %s neighbor at %s on the %s side with hitvec %s\n",
							Helper.coords(floor),
							side.getName(),
							Helper.coords(neighbour),
							opposite.getName(),
							Helper.coords(hitVec)
						);
					MinecraftClient.getInstance().player.swingHand(Hand.MAIN_HAND);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void process() {
		if (Toggled) {
			if (check()) {
				final int holdSlot = MinecraftClient.getInstance().player.inventory.selectedSlot;
				place();
				MinecraftClient.getInstance().player.inventory.selectedSlot = holdSlot;
			}
		}
	}
}
