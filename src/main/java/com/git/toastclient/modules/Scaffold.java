package com.git.toastclient.modules;

import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Scaffold extends Module {

	@Override
	public void process() {
		if (Toggled) {
			if (BlockCheck()) {
				BlockPlace();
			}
		}
	}

	public Scaffold() {
		initKeybind("Scaffold", GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	void BlockPlace() {
		int holdSlot = MinecraftClient.getInstance().player.inventory.selectedSlot;
		if (HoldBlock()) {
			Vec3d eyes = MinecraftClient.getInstance().player.getPos().add(0.0d,
					MinecraftClient.getInstance().player.getEyeHeight(MinecraftClient.getInstance().player.getPose()),
					0.0d);
			MinecraftClient.getInstance().player
					.sendChatMessage(String.format("My eyes are at %f %f %f", eyes.x, eyes.y, eyes.z));
			for (Direction side : Direction.values()) {
				BlockPos floor = (new BlockPos(MinecraftClient.getInstance().player).down());
				MinecraftClient.getInstance().player.sendChatMessage(
						String.format("The floor is at %d %d %d", floor.getX(), floor.getY(), floor.getZ()));
				BlockPos neighbour = floor.offset(side);
				MinecraftClient.getInstance().player.sendChatMessage(String.format("It's %s neighbor is at %d %d %d",
						side.getName(), neighbour.getX(), neighbour.getY(), neighbour.getZ()));
				Direction opposite = side.getOpposite();
				if (eyes.squaredDistanceTo(new Vec3d(floor)) >= eyes.squaredDistanceTo(new Vec3d(neighbour))) {
					continue;
				}
				Vec3d hitVec = new Vec3d(neighbour).add(new Vec3d(opposite.getUnitVector()).multiply(0.5d));
				if (eyes.squaredDistanceTo(hitVec) > 18.0625) {
					continue;
				}
				MinecraftClient.getInstance().interactionManager.interactBlock(MinecraftClient.getInstance().player,
						MinecraftClient.getInstance().world, Hand.MAIN_HAND,
						new BlockHitResult(hitVec, opposite, neighbour.offset(side), true));
				MinecraftClient.getInstance().player.swingHand(Hand.MAIN_HAND);
				// if (MinecraftClient.getInstance().world.getBlockState(neighbour).getBlock())
				MinecraftClient.getInstance().player.inventory.selectedSlot = holdSlot;
				return;
			}
		}
	}

	boolean BlockCheck() {
		BlockPos floor = (new BlockPos(MinecraftClient.getInstance().player).down());
		BlockState floorBlock = MinecraftClient.getInstance().world.getBlockState(floor);

		return floorBlock.isAir();
	}

	boolean HoldBlock() {
		int blockSlot = -1;

		for (int i = 0; i < 9; i++) {
			ItemStack stack = MinecraftClient.getInstance().player.inventory.getInvStack(i);
			if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
				continue;
			}

			Block b = Block.getBlockFromItem(stack.getItem());
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
}
