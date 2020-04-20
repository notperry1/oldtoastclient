package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.utils.ToastLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdCI extends Command {

    @Override
    public String getName() {
        return "clearinventory";
    }

    @Override
    public String getDescription() {
        return "Clears inventory (Creative)";
    }

    @Override
    public String getSyntax() {
        return "clearinventory";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("clearinv", "clear", "ci");
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (mc.player == null) return;
        for (int i = 0; i < 200; i++) {
            if (mc.interactionManager.getCurrentGameMode() == GameMode.CREATIVE) {
                mc.player.inventory.setInvStack(i, new ItemStack(null));
            } else {
                ToastLogger.errorMessage("Bruh you're not in creative.");
                return;
            }
        }
        ToastLogger.infoMessage("Cleared all items");
    }

}
