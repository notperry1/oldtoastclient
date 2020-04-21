package toast.client.module.mods.combat;

import com.google.common.eventbus.Subscribe;
import net.minecraft.container.SlotActionType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenContainerS2CPacket;
import toast.client.event.events.EventReadPacket;
import toast.client.event.events.EventTick;
import toast.client.gui.clickgui.SettingBase;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.utils.ToastLogger;

import java.util.*;

public class OffhandGap extends Module {

    public OffhandGap() {
        super("OffhandGap", -1, Category.COMBAT, "Places Gaps in offhand when holding Use",
                new SettingSlider("Max Health", 1, 36, 20, 0));
    }

    static ItemStack lastOffhand = null;

    @Subscribe
    public void onTick(EventTick event) {
        if (mc.player == null || mc.player.getHealth() > (float) getSettings().get(0).toSlider().getValue() || mc.player.getOffHandStack().getItem() ==
        Items.ENCHANTED_GOLDEN_APPLE || mc.interactionManager == null) { return; }
        List<Integer> gapSlots = new ArrayList<>(Arrays.asList());
        if (mc.options.keyUse.isPressed()) {
            getInventory().forEach((key, value) -> {
                if (value == Items.ENCHANTED_GOLDEN_APPLE) gapSlots.add(key);
            });
            if (mc.player.getOffHandStack().getItem() != Items.AIR) {
                lastOffhand = mc.player.getOffHandStack();
                mc.interactionManager.clickSlot(0, -106, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(0, mc.player.inventory.getEmptySlot(), 0, SlotActionType.PICKUP, mc.player);
            }
            mc.interactionManager.clickSlot(0, gapSlots.get(0), 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(0, -106, 0, SlotActionType.PICKUP, mc.player);
        } else {
            if (lastOffhand != null) {
                mc.interactionManager.clickSlot(0, mc.player.inventory.getSlotWithStack(lastOffhand), 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(0, -106, 0, SlotActionType.PICKUP, mc.player);
            }
        }
    }

    public LinkedHashMap<Integer, Item> getInventory() {
        if (mc.player == null) return null;

        LinkedHashMap<Integer, Item> items = new LinkedHashMap<Integer, Item>();
        for (int i = 0; i < 35; i++) {
            if (mc.player.inventory.getInvStack(i) != null && mc.player.inventory.getInvStack(i).getItem() != Items.AIR) {
                items.put(i, mc.player.inventory.getInvStack(i).getItem());
            }
        }
        return items.equals(new LinkedHashMap<Integer, Item>()) ? null : items;
    }
}
