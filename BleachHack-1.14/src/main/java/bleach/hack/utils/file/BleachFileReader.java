package bleach.hack.utils.file;

import java.util.List;

import bleach.hack.gui.clickgui.ClickGuiScreen;
import bleach.hack.gui.clickgui.SettingBase;
import bleach.hack.gui.clickgui.SettingMode;
import bleach.hack.gui.clickgui.SettingSlider;
import bleach.hack.gui.clickgui.SettingToggle;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ClickGui;

public class BleachFileReader {

	private BleachFileMang fileMang = new BleachFileMang();
	
	public BleachFileReader() {
	}
	
	public void saveModules() {
		fileMang.createEmptyFile("modules.txt");
		
		for(Module m: ModuleManager.getModules()) {
			if(m.getName() == "ClickGui" || m.getName() == "Freecam") continue;
			fileMang.appendFile("modules.txt", m.getName() + ":" + Boolean.toString(m.isToggled()));
		}
	}
	
	public void readModules() {
		List<String> lines = fileMang.readFileLines("modules.txt");
		
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
	
	public void saveSettings() {
		fileMang.createEmptyFile("settings.txt");
		
		for(Module m: ModuleManager.getModules()) {
			String line = m.getName();
			int count = 0;
			
			for(SettingBase set: m.getSettings()) {
				if(set instanceof SettingSlider) line += ":" + m.getSettings().get(count).toSlider().getValue();
				if(set instanceof SettingMode) line += ":" + m.getSettings().get(count).toMode().mode;
				if(set instanceof SettingToggle) line += ":" + m.getSettings().get(count).toToggle().state;
				count++;
			}
			
			fileMang.appendFile("settings.txt", line);
		}
	}
	
	public void readSettings() {
		List<String> lines = fileMang.readFileLines("settings.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				int count = 0;
				
				for(SettingBase set: m.getSettings()) {
					try {
						if(line[0].contains(m.getName())) {
							if(set instanceof SettingSlider) {
								m.getSettings().get(count).toSlider().value = Double.parseDouble(line[count+1]);}
							if(set instanceof SettingMode) {
								m.getSettings().get(count).toMode().mode = Integer.parseInt(line[count+1]);}
							if(set instanceof SettingToggle) {
								m.getSettings().get(count).toToggle().state = Boolean.parseBoolean(line[count+1]);}
						}
					}catch(Exception e) {}
					count++;
				}
			}
		}
	}
	
	public void saveClickGui() {
		fileMang.createEmptyFile("clickgui.txt");
		
		ClickGuiScreen gui = ClickGui.clickGui;
		
		fileMang.appendFile("clickgui.txt", gui.modsCmb.getPos()[0] + ":" + gui.modsCmb.getPos()[1]);
		fileMang.appendFile("clickgui.txt", gui.modsExp.getPos()[0] + ":" + gui.modsExp.getPos()[1]);
		fileMang.appendFile("clickgui.txt", gui.modsMsc.getPos()[0] + ":" + gui.modsMsc.getPos()[1]);
		fileMang.appendFile("clickgui.txt", gui.modsMvm.getPos()[0] + ":" + gui.modsMvm.getPos()[1]);
		fileMang.appendFile("clickgui.txt", gui.modsPly.getPos()[0] + ":" + gui.modsPly.getPos()[1]);
		fileMang.appendFile("clickgui.txt", gui.modsRen.getPos()[0] + ":" + gui.modsRen.getPos()[1]);
	}
	
	public void readClickGui() {
		List<String> lines = fileMang.readFileLines("clickgui.txt");
		
		ClickGuiScreen gui = ClickGui.clickGui;
		
		try {
			gui.modsCmb.setPos(Integer.parseInt(lines.get(0).split(":")[0]), Integer.parseInt(lines.get(0).split(":")[1]));
			gui.modsExp.setPos(Integer.parseInt(lines.get(1).split(":")[0]), Integer.parseInt(lines.get(1).split(":")[1]));
			gui.modsMsc.setPos(Integer.parseInt(lines.get(2).split(":")[0]), Integer.parseInt(lines.get(2).split(":")[1]));
			gui.modsMvm.setPos(Integer.parseInt(lines.get(3).split(":")[0]), Integer.parseInt(lines.get(3).split(":")[1]));
			gui.modsPly.setPos(Integer.parseInt(lines.get(4).split(":")[0]), Integer.parseInt(lines.get(4).split(":")[1]));
			gui.modsRen.setPos(Integer.parseInt(lines.get(5).split(":")[0]), Integer.parseInt(lines.get(5).split(":")[1]));
		}catch(Exception e) {}
	}
}
