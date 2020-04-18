package toast.client.utils.file;

import java.util.List;

import toast.client.command.CommandManager;
import toast.client.gui.clickgui.SettingBase;
import toast.client.gui.clickgui.SettingMode;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.gui.clickgui.SettingToggle;
import toast.client.gui.window.Window;
import toast.client.module.Module;
import toast.client.module.ModuleManager;
import toast.client.module.mods.render.ClickGui;
import net.minecraft.util.math.MathHelper;

public class ToastFileHelper {

	public static void saveModules() {
		ToastFileMang.createEmptyFile("modules.txt");
		
		String lines = "";
		for(Module m: ModuleManager.getModules()) {
			if(m.getName() == "ClickGui" || m.getName() == "Freecam") continue;
			lines += m.getName() + ":" + m.isToggled() + "\n";
		}
		
		ToastFileMang.appendFile(lines, "modules.txt");
	}
	
	public static void readModules() {
		List<String> lines = ToastFileMang.readFileLines("modules.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				try {
					if(line[0].contains(m.getName()) && line[1].contains("true")) {
						m.toggle();
						break;
					}
				}catch(Exception e) {}
			}
		}
	}
	
	public static void saveSettings() {
		ToastFileMang.createEmptyFile("settings.txt");
		
		String lines = "";
		for(Module m: ModuleManager.getModules()) {
			String line = m.getName();
			int count = 0;
			
			for(SettingBase set: m.getSettings()) {
				if(set instanceof SettingSlider) line += ":" + m.getSettings().get(count).toSlider().getValue();
				if(set instanceof SettingMode) line += ":" + m.getSettings().get(count).toMode().mode;
				if(set instanceof SettingToggle) line += ":" + m.getSettings().get(count).toToggle().state;
				count++;
			}
			lines += line + "\n";
		}
		
		ToastFileMang.appendFile(lines, "settings.txt");
	}
	
	public static void readSettings() {
		List<String> lines = ToastFileMang.readFileLines("settings.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				if(!line[0].startsWith(m.getName())) continue;
				int count = 0;
				
				for(SettingBase set: m.getSettings()) {
					try {
						if(set instanceof SettingSlider) {
							m.getSettings().get(count).toSlider().setValue(Double.parseDouble(line[count+1]));}
						if(set instanceof SettingMode) {
							m.getSettings().get(count).toMode().mode = MathHelper.clamp(Integer.parseInt(line[count+1]),
									0, m.getSettings().get(count).toMode().modes.length - 1);}
						if(set instanceof SettingToggle) {
							m.getSettings().get(count).toToggle().state = Boolean.parseBoolean(line[count+1]);}
					}catch(Exception e) {}
					count++;
				}
			}
		}
	}
	
	public static void saveBinds() {
		ToastFileMang.createEmptyFile("binds.txt");
		
		String lines = "";
		for(Module m: ModuleManager.getModules()) {
			lines += m.getName() + ":" + m.getKey() + "\n";
		}
		
		ToastFileMang.appendFile(lines, "binds.txt");
	}
	
	public static void readBinds() {
		List<String> lines = ToastFileMang.readFileLines("binds.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				if(!line[0].startsWith(m.getName())) continue;
				try { m.setKey(Integer.parseInt(line[line.length - 1])); }catch(Exception e) {}
			}
		}
	}
	
	public static void saveClickGui() {
		ToastFileMang.createEmptyFile("clickgui.txt");
		
		String text = "";
		for(Window w: ClickGui.clickGui.windows) text += w.x1 + ":" + w.y1 + "\n";
		
		ToastFileMang.appendFile(text, "clickgui.txt");
	}
	
	public static void readClickGui() {
		List<String> lines = ToastFileMang.readFileLines("clickgui.txt");
		
		try {
			int c = 0;
			for(Window w: ClickGui.clickGui.windows) {
				w.x1 = Integer.parseInt(lines.get(c).split(":")[0]);
				w.y1 = Integer.parseInt(lines.get(c).split(":")[1]);
				c++;
			}
		}catch(Exception e) {}
	}
	
	public static void readPrefix() {
		try{ CommandManager.prefix = ToastFileMang.readFileLines("prefix.txt").get(0); }catch(Exception e) {}
	}
	
}
