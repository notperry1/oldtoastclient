package toast.client.module.mods.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import toast.client.event.events.Event3DRender;
import toast.client.event.events.EventEntityRender;
import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.RenderUtils;
import toast.client.utils.WorldUtils;

import java.util.Arrays;
import java.util.List;

public class HoleESP extends Module {

    private List<BlockPos> offsets = Arrays.asList(
            new BlockPos(0, -1, 0),
            new BlockPos(1, 0, 0),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(0, 0, -1)
    );

    public HoleESP() {
        super("HoleESP", -1, Category.RENDER, "Renders safe spots to stand in during crystal fights",
                new SettingSlider("Range", 5, 20, 8, 0),
                new SettingToggle("Bedrock (Green)", true),
                new SettingToggle("Mixed (Yellow)", true),
                new SettingToggle("Obsidian (Red)", true),
                new SettingSlider("Opacity", 1, 100, 5, 0),
                new SettingMode("Mode: ", "Flat", "Flat", "Box"));
    }

    @Subscribe
    public void onRender(Event3DRender event) {
        if (mc.player == null || mc.world == null) return;
        List<BlockPos> blockPositions = WorldUtils.getBlockPositionsInArea(mc.player.getBlockPos().add(-getSettings().get(0).toSlider().getValue(), -getSettings().get(0).toSlider().getValue(), -getSettings().get(0).toSlider().getValue()), mc.player.getBlockPos().add(getSettings().get(0).toSlider().getValue(), getSettings().get(0).toSlider().getValue(), getSettings().get(0).toSlider().getValue()));
        blockPositions.stream()
                .filter(pos -> new Vec3d(pos.getX(), pos.getY(), pos.getZ()).distanceTo(mc.player.getPos()) < getSettings().get(0).toSlider().getValue())
                .filter(pos -> mc.world.getBlockState(pos).getBlock() == Blocks.AIR)
                .filter(pos -> {
                    int acceptable = 0;
                    for (BlockPos blockPos : offsets) {
                        if (((getSettings().get(1).toToggle().state || getSettings().get(2).toToggle().state) && mc.world.getBlockState(pos.add(blockPos)).getBlock() == Blocks.BEDROCK) ||
                                ((getSettings().get(3).toToggle().state || getSettings().get(2).toToggle().state) && mc.world.getBlockState(pos.add(blockPos)).getBlock() == Blocks.OBSIDIAN)) {
                            acceptable++;
                        }
                    }
                    return acceptable == 5 &&
                            (mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.AIR || mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.CAVE_AIR) &&
                            (mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() == Blocks.AIR || mc.world.getBlockState(pos.add(0, 2, 0)).getBlock() == Blocks.CAVE_AIR);
                })
                .forEach(pos -> {
                    int bedrock = 0;
                    int obsidian = 0;

                    for (BlockPos blockPos : offsets) {
                        if (((getSettings().get(1).toToggle().state || getSettings().get(2).toToggle().state) && mc.world.getBlockState(pos.add(blockPos)).getBlock() == Blocks.BEDROCK)) {
                            bedrock++;
                        }
                        else if ((getSettings().get(3).toToggle().state || getSettings().get(2).toToggle().state) && mc.world.getBlockState(pos.add(blockPos)).getBlock() == Blocks.OBSIDIAN) {
                            obsidian++;
                        }
                    }

                    if (getSettings().get(5).toMode().mode == 1) {
                        if (bedrock == 5) {
                            RenderUtils.drawFilledBox(pos, 0.08f, 1f, 0.35f, (float) (getSettings().get(4).toSlider().getValue() + 20) / 100);
                        } else if (obsidian == 5) {
                            RenderUtils.drawFilledBox(pos, 1f, 0f, 0f, (float) (getSettings().get(4).toSlider().getValue() + 20) / 100);
                        } else {
                            RenderUtils.drawFilledBox(pos, 1f, 1f, 0f, (float) (getSettings().get(4).toSlider().getValue() + 20) / 100);
                        }
                    } else if (getSettings().get(5).toMode().mode == 0) {
                        if (bedrock == 5) {
                            RenderUtils.drawFilledBox(new Box(Math.floor(pos.getX()), Math.floor(pos.getY()), Math.floor(pos.getZ()), Math.ceil(pos.getX() + 0.01), Math.floor(pos.getY()) - 0.0001, Math.ceil(pos.getZ() + 0.01)), 0.08f, 1f, 0.35f, (float) (getSettings().get(4).toSlider().getValue() + 20) / 100);
                            //RenderUtils.drawQuad(Math.floor(pos.getX()), Math.floor(pos.getZ()), Math.ceil(pos.getX()), Math.ceil(pos.getZ()), Math.floor(pos.getY()), 0.08f, 1f, 0.35f, (float) getSettings().get(4).toSlider().getValue() / 100);
                        } else if (obsidian == 5) {
                            RenderUtils.drawFilledBox(new Box(Math.floor(pos.getX()), Math.floor(pos.getY()), Math.floor(pos.getZ()), Math.ceil(pos.getX() + 0.01), Math.floor(pos.getY()) - 0.0001, Math.ceil(pos.getZ() + 0.01)), 1f, 0f, 0f, (float) (getSettings().get(4).toSlider().getValue() + 20) / 100);
                            //RenderUtils.drawQuad(Math.floor(pos.getX()), Math.floor(pos.getZ()), Math.ceil(pos.getX()), Math.ceil(pos.getZ()), Math.floor(pos.getY()), 1f, 1f, 0f, (float) getSettings().get(4).toSlider().getValue() / 100);
                        } else {
                            RenderUtils.drawFilledBox(new Box(Math.floor(pos.getX()), Math.floor(pos.getY()), Math.floor(pos.getZ()), Math.ceil(pos.getX() + 0.01), Math.floor(pos.getY()) - 0.0001, Math.ceil(pos.getZ() + 0.01)), 1f, 1f, 0f, (float) (getSettings().get(4).toSlider().getValue() + 20) / 100);
                            //RenderUtils.drawQuad(Math.floor(pos.getX()), Math.floor(pos.getZ()), Math.ceil(pos.getX()), Math.ceil(pos.getZ()), Math.floor(pos.getY()), 1f, 0f, 0f, (float) getSettings().get(4).toSlider().getValue() / 100);
                        }
                    }
                });
    }
}
