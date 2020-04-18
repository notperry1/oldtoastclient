package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.command.CommandManager;
import toast.client.utils.BleachLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;

public class CmdNBT extends Command {

    @Override
    public String getAlias() {
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
    public void onCommand(String command, String[] args) throws Exception {
        if(args[0].isEmpty()) {
            BleachLogger.errorMessage("Invalid Syntax!");
            BleachLogger.infoMessage(CommandManager.prefix + getSyntax());
            return;
        }
        ItemStack item = mc.player.inventory.getMainHandStack();

        if (args[0].equalsIgnoreCase("get")) {
        	BleachLogger.infoMessage("§6§lNBT:\n" + item.getTag() + "");
        }else if (args[0].equalsIgnoreCase("copy")) {
            mc.keyboard.setClipboard(item.getTag() + "");
            BleachLogger.infoMessage("§6Copied\n§f" + (item.getTag() + "\n") + "§6to clipboard.");
        }else if (args[0].equalsIgnoreCase("set")) {
            try {
                if (args[1].isEmpty()) {
                    BleachLogger.errorMessage("Invalid Syntax!");
                    BleachLogger.infoMessage(CommandManager.prefix + getSyntax());
                    return;
                }
                item.setTag(StringNbtReader.parse(args[1]));
                BleachLogger.infoMessage("§6Set NBT of " + item.getItem().getName() + "to\n§f" + (item.getTag()));
            } catch (Exception e) {
                BleachLogger.errorMessage("Invalid Syntax!");
                BleachLogger.infoMessage(getSyntax());
            }
        }else if (args[0].equalsIgnoreCase("wipe")) {
            item.setTag(new CompoundTag());
        }

    }

}
