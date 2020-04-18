package toast.client.command;

import java.util.Arrays;
import java.util.List;

import toast.client.command.commands.*;
import toast.client.utils.ToastLogger;

public class CommandManager {

	public static String prefix = ".";
	
	private static List<Command> commands = Arrays.asList(
			new CmdBind(),
			new CmdCI(),
			new CmdEntityStats(),
			new CmdGuiReset(),
			new CmdHelp(),
			new CmdNBT(),
			new CmdPeek(),
			new CmdPrefix(),
			new CmdRbook(),
			new CmdSetting(),
			new CmdTeleport(),
			new CmdToggle(),
			new CmdVanish());
	
	public static List<Command> getCommands(){
		return commands;
	}
	
	public static void callCommand(String input) {
		String[] split = input.split(" ");
		System.out.println(Arrays.asList(split));
		String command = split[0];
		String args = input.substring(command.length()).trim();
		for(Command c: getCommands()) {
			if(c.getAlias().equalsIgnoreCase(command)) {
				try {
					c.onCommand(command, args.split(" "));
				}catch(Exception e) {
					e.printStackTrace();
					ToastLogger.errorMessage("Invalid Syntax!");
					ToastLogger.infoMessage(prefix + c.getSyntax());
				}
				return;
			}
		}
		ToastLogger.errorMessage("Command Not Found, Maybe Try .Help");
	}
}