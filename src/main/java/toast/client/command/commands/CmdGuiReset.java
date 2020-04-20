package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.module.mods.render.ClickGui;
import toast.client.utils.ToastLogger;

import java.util.Collections;
import java.util.List;

public class CmdGuiReset extends Command {

	@Override
	public String getName() {
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
	public List<String> getAliases() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		ClickGui.clickGui.resetGui();
		ToastLogger.infoMessage("Reset the clickgui!");
	}

}
