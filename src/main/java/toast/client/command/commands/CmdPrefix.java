package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.command.CommandManager;
import toast.client.utils.ToastLogger;
import toast.client.utils.file.ToastFileMang;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdPrefix extends Command {

	@Override
	public String getName() {
		return "prefix";
	}

	@Override
	public String getDescription() {
		return "Sets the command prefix";
	}

	@Override
	public String getSyntax() {
		return "prefix [Char]";
	}

	@Override
	public List<String> getAliases() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0].isEmpty()) {
			ToastLogger.errorMessage("Prefix Cannot Be Empty");
			return;
		}
		
		ToastFileMang.createEmptyFile("prefix.txt");
		ToastFileMang.appendFile(args[0], "prefix.txt");
		CommandManager.prefix = args[0];
		ToastLogger.infoMessage("Set Prefix To: \"" + args[0] + "\"");
	}

}
