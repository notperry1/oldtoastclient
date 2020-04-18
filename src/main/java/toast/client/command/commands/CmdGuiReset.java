package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.module.mods.ClickGui;
import toast.client.utils.ToastLogger;

public class CmdGuiReset extends Command {

	@Override
	public String getAlias() {
		return "guireset";
	}

	@Override
	public String getDescription() {
		return "Resets the clickgui windows";
	}

	@Override
	public String getSyntax() {
		return "guireset";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		ClickGui.clickGui.resetGui();
		ToastLogger.infoMessage("Reset the clickgui!");
	}

}
