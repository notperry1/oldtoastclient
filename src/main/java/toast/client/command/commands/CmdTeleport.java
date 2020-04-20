package toast.client.command.commands;

import toast.client.ToastClient;
import toast.client.command.Command;
import toast.client.module.ModuleManager;
import toast.client.module.mods.hidden.Teleport;
import toast.client.utils.ToastLogger;
import net.minecraft.util.math.Vec3d;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdTeleport extends Command {

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getDescription() {
        return "Teleport yourself at\nspecified speed.";
    }

    @Override
    public String getSyntax() {
        return "teleport [(~)x] [(~)y] [(~)z] [blocks per teleport] | teleport stop";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("tp");
    }

    DecimalFormat df = new DecimalFormat("#.###");

    public void onCommand(final String command, final String[] args) throws Exception {
        if (mc.player == null) return;
    	if (args[0].equalsIgnoreCase("stop")) {
            ToastLogger.warningMessage("Teleport Cancelled!");
            ToastClient.eventBus.unregister(ModuleManager.getModule(Teleport.class));
            return;
        }
        if (args.length >= 3) {
            try {
                final double x = args[0].equals("~") ? mc.player.getPos().getX() : args[0].charAt(0) == '~' ? Double.parseDouble(args[0].substring(1)) + mc.player.getPos().getX() : Double.parseDouble(args[0]);
                final double y = args[1].equals("~") ? mc.player.getPos().getY() : args[1].charAt(0) == '~' ? Double.parseDouble(args[1].substring(1)) + mc.player.getPos().getY() : Double.parseDouble(args[1]);
                final double z = args[2].equals("~") ? mc.player.getPos().getZ() : args[2].charAt(0) == '~' ? Double.parseDouble(args[2].substring(1)) + mc.player.getPos().getZ() : Double.parseDouble(args[2]);
                Teleport.finalPos = new Vec3d(x, y, z);
                Teleport.blocksPerTeleport = args.length == 3 ? Teleport.finalPos.distanceTo(mc.player.getPosVector()) : Double.parseDouble(args[3]);
                ModuleManager.getModule(Teleport.class).setToggled(true);
                ToastLogger.infoMessage("\n§aTeleporting to \n§cX: §b" + df.format(x) + "§a, \n§cY: §b" + df.format(y) + "§a, \n§cZ: §b" + df.format(z) + "\n§aat §b" + df.format(ModuleManager.getModule(Teleport.class).getSettings().get(0).toSlider().getValue()) + "§c blocks per teleport.");
            }
            catch (NullPointerException e){
                ToastLogger.warningMessage("Null Pointer Exception Caught!");
            }

        }
        else {
            ToastLogger.errorMessage(getSyntax());
        }
    }

}
