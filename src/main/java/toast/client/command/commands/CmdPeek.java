package toast.client.command.commands;

import java.util.List;

import toast.client.command.Command;
import toast.client.utils.ToastLogger;
import toast.client.utils.ToastQueue;
import toast.client.utils.ItemContentUtils;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class CmdPeek extends Command {

	@Override
	public String getAlias() {
		return "peek";
	}

	@Override
	public String getDescription() {
		return "Shows whats inside a container";
	}

	@Override
	public String getSyntax() {
		return "peek";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if (mc.player == null) return;
		ItemStack item = mc.player.inventory.getMainHandStack();
		
		if(!(item.getItem() instanceof BlockItem)) {
			ToastLogger.errorMessage("Must be holding a containter to peek.");
			return;
		}
		
		if(!(((BlockItem) item.getItem()).getBlock() instanceof ShulkerBoxBlock)
				 && !(((BlockItem) item.getItem()).getBlock() instanceof ChestBlock)
				 && !(((BlockItem) item.getItem()).getBlock() instanceof DispenserBlock)
				 && !(((BlockItem) item.getItem()).getBlock() instanceof HopperBlock)) {
			ToastLogger.errorMessage("Must be holding a containter to peek.");
			return;
		}
		
		List<ItemStack> items = ItemContentUtils.getItemsInContainer(item);
		
		BasicInventory inv = new BasicInventory(items.toArray(new ItemStack[27]));
		
		ToastQueue.queue.add(() -> {
			mc.openScreen(new ShulkerBoxScreen(
					new ShulkerBoxContainer(420, mc.player.inventory, inv),
					mc.player.inventory,
					item.getName()));
		});
	}

}
