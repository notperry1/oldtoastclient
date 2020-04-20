package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.utils.ToastLogger;
import net.minecraft.client.util.InputUtil;

import java.util.Collections;
import java.util.List;

public class CmdBind extends Command {

	@Override
	public String getName() {
		return "bind";
	}

	@Override
	public String getDescription() {
		return "Binds a module";
	}

	@Override
	public String getSyntax() {
		return "bind add [Module] [Key] | .bind del [Module]";
	}

	@Override
	public List<String> getAliases() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		for(Module m: ModuleManager.getModules()) {
			if(m.getName().equalsIgnoreCase(args[1])) {
				if(args[0].equalsIgnoreCase("add")) {
					m.setKey(InputUtil.fromName("key.keyboard." + args[2].toLowerCase()).getKeyCode());
					ToastLogger.infoMessage("Bound " + m.getName() + " To " + args[2]);
				}else if(args[0].equalsIgnoreCase("del")) {
					m.setKey(-1);
					ToastLogger.infoMessage("Removed Bind For " + m.getName());
				}
				return;
			}
		}
		ToastLogger.errorMessage("Could Not Find Module \"" + args[1] + "\"");
	}

}
