package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.utils.BleachLogger;
import toast.client.utils.BleachQueue;

public class CmdToggle extends Command {

	@Override
	public String getAlias() {
		return "toggle";
	}

	@Override
	public String getDescription() {
		return "Toggles a mod with a command.";
	}

	@Override
	public String getSyntax() {
		return "toggle [Module]";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		for(Module m: ModuleManager.getModules()) {
			if(args[0].equalsIgnoreCase(m.getName())) {
				BleachQueue.queue.add(() -> { m.toggle(); });
				BleachLogger.infoMessage(m.getName() + " Toggled");
				return;
			}
		}
		BleachLogger.errorMessage("Module \"" + args[0] + "\" Not Found!");
	}

}
