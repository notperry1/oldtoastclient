package toast.client.command.commands;

import net.minecraft.util.math.Vec3d;
import toast.client.command.Command;
import toast.client.module.mods.render.Tracers;
import toast.client.utils.ToastLogger;

import java.util.Arrays;
import java.util.List;

public class CmdWaypoint extends Command {
    @Override
    public String getName() {
        return "waypoints";
    }

    @Override
    public String getDescription() {
        return "Add, remove, and modify waypoints";
    }

    @Override
    public String getSyntax() {
        return "waypoints <add/delete/modify> <name> [x] [y] [z]";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("waypoint", "wp");
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (mc.player == null) return;
        String option = args[0];
        String name = args[1];
        double x = Double.NaN;
        double y = Double.NaN;
        double z = Double.NaN;

        if (option.equalsIgnoreCase("add") || option.equalsIgnoreCase("modify")) {
            try {
                x = Double.parseDouble(args[2]);
                y = Double.parseDouble(args[3]);
                z = Double.parseDouble(args[4]);
            } catch (Throwable t) {
                ToastLogger.infoMessage("Position of waypoint not given, using player position.");
                x = mc.player.getX();
                y = mc.player.getY();
                z = mc.player.getZ();
            }
        }

        if (args[1] == null) return;

        switch (option) {
            case "add":
                Tracers.addWaypoint(name, new Vec3d(x, y, z));
            case "delete":
                Tracers.removeWaypoint(name);
            case "modify":
                Tracers.removeWaypoint(name);
                Tracers.addWaypoint(name, new Vec3d(x, y, z));
        }
    }
}
