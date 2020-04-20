package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.utils.ToastLogger;
import net.minecraft.entity.Entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdVanish extends Command {

	private static Entity vehicle;
    
    @Override
    public String getName() {
        return "vanish";
    }

    @Override
    public String getDescription() {
        return "Entity Desynchronisation.";
    }

    @Override
    public String getSyntax() {
        return "vanish";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("v", "entityvanish", "ev", "entitydesync");
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.getVehicle() != null && vehicle == null) {
            vehicle = mc.player.getVehicle();
            mc.player.stopRiding();
            mc.world.removeEntity(vehicle.getEntityId());
            ToastLogger.infoMessage("Vehicle " + vehicle.getName().asString() + " removed.");
        } else {
            if (vehicle != null) {
                vehicle.removed = false;
                mc.world.addEntity(vehicle.getEntityId(), vehicle);
                mc.player.startRiding(vehicle, true);
                ToastLogger.infoMessage("Vehicle " + vehicle.getName().asString() + " created.");
                vehicle = null;
            } else {
                ToastLogger.errorMessage("No Vehicle.");
            }
        }
    }
}
