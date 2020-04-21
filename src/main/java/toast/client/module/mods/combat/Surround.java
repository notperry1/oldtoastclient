package toast.client.module.mods.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import toast.client.event.events.EventTick;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.WorldUtils;

public class Surround extends Module {

    public Surround() {
        super("Surround", -1, Category.COMBAT, "Surrounds yourself with obsidian.",
                new SettingSlider("Blocks/Tick", 1, 4, 1, 0));
    }

    @Subscribe
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) return;

        int slot = -1;
        int prevSlot = mc.player.inventory.selectedSlot;

        if(mc.player.inventory.getMainHandStack().getItem() == Items.OBSIDIAN) {
            slot = mc.player.inventory.selectedSlot;
        } else for(int i = 0; i < 9; i++) {
            if(mc.player.inventory.getInvStack(i).getItem() == Items.OBSIDIAN) {
                slot = i;
                break;
            }
        }

        if(slot == -1) return;

        double x = mc.player.getPos().getX();
        double z = mc.player.getPos().getZ();
        if (x < 0) {
            x = Math.ceil(x) - 0.5d;
        } else {
            x = Math.floor(x) + 0.5d;
        }
        if (z < 0) {
            z = Math.ceil(z) - 0.5d;
        } else {
            z = Math.floor(z) + 0.5d;
        }
        mc.player.updatePosition(x, mc.player.getY(), z);

        for (int i = 0; i < getSettings().get(0).toSlider().getValue(); i++) {
            if (WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(new BlockPos(1, 0, 0).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ())).getBlock())) {
                mc.player.inventory.selectedSlot = slot;
                placeBlockAuto(new BlockPos(1, 0, 0).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ()));
            } else if (WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(new BlockPos(-1, 0, 0).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ())).getBlock())) {
                mc.player.inventory.selectedSlot = slot;
                placeBlockAuto(new BlockPos(-1, 0, 0).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ()));
            } else if (WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(new BlockPos(0, 0, 1).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ())).getBlock())) {
                mc.player.inventory.selectedSlot = slot;
                placeBlockAuto(new BlockPos(0, 0, 1).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ()));
            } else if (WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(new BlockPos(0, 0, -1).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ())).getBlock())) {
                mc.player.inventory.selectedSlot = slot;
                placeBlockAuto(new BlockPos(0, 0, -1).add(mc.player.getPos().getX(), mc.player.getPos().getY(), mc.player.getPos().getZ()));
            } else {
                mc.player.inventory.selectedSlot = prevSlot;
                setToggled(false);
            }
        }
    }

    public boolean placeBlockAuto(BlockPos block) {
        for(Direction d: Direction.values()) {
            if(!WorldUtils.NONSOLID_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
                if(WorldUtils.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

                }
                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND,
                        new BlockHitResult(new Vec3d(block), d.getOpposite(), block.offset(d), false));
                mc.player.swingHand(Hand.MAIN_HAND);
                if(WorldUtils.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
                }
                return true;
            }
        }

        return false;
    }
}
