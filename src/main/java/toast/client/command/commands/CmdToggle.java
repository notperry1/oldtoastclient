package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.utils.ToastLogger;
import toast.client.utils.ToastQueue;

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
				ToastQueue.queue.add(() -> { m.toggle(); });
				ToastLogger.infoMessage(m.getName() + " Toggled");
				return;
			}
		}
		ToastLogger.errorMessage("Module \"" + args[0] + "\" Not Found!");
	}

}
