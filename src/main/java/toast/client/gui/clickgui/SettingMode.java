package toast.client.gui.clickgui;

public class SettingMode extends SettingBase {

	public String[] modes;
	public String defaultMode;
	public int mode;
	public String text;
	
	public SettingMode(String text, String defaultMode, String... modes) {
		this.modes = modes;
		this.defaultMode = defaultMode;
		this.text = text;
	}
	
	public int getNextMode() {
		if(mode + 1 >= modes.length) {
			return 0;
		}
		return mode+1;
	}
}
