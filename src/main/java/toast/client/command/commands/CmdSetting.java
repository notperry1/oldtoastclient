package toast.client.command.commands;

import toast.client.command.Command;
import toast.client.gui.clickgui.SettingBase;
import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.utils.BleachLogger;

public class CmdSetting extends Command {

	@Override
	public String getAlias() {
		return "setting";
	}

	@Override
	public String getDescription() {
		return "Changes a setting in a module";
	}

	@Override
	public String getSyntax() {
		return "setting [Module] [Setting number (starts at 0)] [value]";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args.length < 2) {
			BleachLogger.errorMessage(getSyntax());
			return;
		}
		
		Module m = ModuleManager.getModuleByName(args[0]);
		SettingBase s = m.getSettings().get(Integer.parseInt(args[1]));
		
		if(s instanceof SettingSlider) s.toSlider().setValue(Double.parseDouble(args[2]));
		else if(s instanceof SettingToggle) s.toToggle().state = Boolean.valueOf(args[2]);
		else if(s instanceof SettingMode) s.toMode().mode = Integer.parseInt(args[2]);
		else {
			BleachLogger.errorMessage("Invalid Command");
			return;
		}
		
		BleachLogger.infoMessage("Set Setting " + args[1] + " Of " + m.getName() + " To " + args[2]);
	}

}
