package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.command.CommandManager;
import toast.client.utils.ToastLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;

import java.util.Collections;
import java.util.List;

public class CmdNBT extends Command {

    @Override
    public String getName() {
        return "nbt";
    }

    @Override
    public String getDescription() {
        return "NBT stuff";
    }

    @Override
    public String getSyntax() {
        return "nbt [get/copy/set/wipe] <nbt>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (mc.player == null) return;
        if(args[0].isEmpty()) {
            ToastLogger.errorMessage("Invalid Syntax!");
            ToastLogger.infoMessage(CommandManager.prefix + getSyntax());
            return;
        }
        ItemStack item = mc.player.inventory.getMainHandStack();

        if (args[0].equalsIgnoreCase("get")) {
        	ToastLogger.infoMessage("§6§lNBT:\n" + item.getTag() + "");
        }else if (args[0].equalsIgnoreCase("copy")) {
            mc.keyboard.setClipboard(item.getTag() + "");
            ToastLogger.infoMessage("§6Copied\n§f" + (item.getTag() + "\n") + "§6to clipboard.");
        }else if (args[0].equalsIgnoreCase("set")) {
            try {
                if (args[1].isEmpty()) {
                    ToastLogger.errorMessage("Invalid Syntax!");
                    ToastLogger.infoMessage(CommandManager.prefix + getSyntax());
                    return;
                }
                item.setTag(StringNbtReader.parse(args[1]));
                ToastLogger.infoMessage("§6Set NBT of " + item.getItem().getName() + "to\n§f" + (item.getTag()));
            } catch (Exception e) {
                ToastLogger.errorMessage("Invalid Syntax!");
                ToastLogger.infoMessage(getSyntax());
            }
        }else if (args[0].equalsIgnoreCase("wipe")) {
            item.setTag(new CompoundTag());
        }

    }

}
