package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.command.CommandManager;
import toast.client.utils.ToastLogger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdHelp extends Command {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Displays all the commands";
	}

	@Override
	public String getSyntax() {
		return "help / .help [Command]";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("h", "cmd", "cmds", "?");
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		String cmd = null;
		try { cmd = args[0]; } catch(Exception ignored) {}

		ToastLogger.noPrefixMessage("§eHelp");
		for(Command c: CommandManager.getCommands()) {
			if(!cmd.isEmpty() && !cmd.equalsIgnoreCase(c.getName())) continue;
			ToastLogger.noPrefixMessage("§5." + c.getName() + " | §6" + c.getDescription() + " | §e" + c.getSyntax());
		}
	}

}
