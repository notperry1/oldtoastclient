package toast.client.module.mods.render;

import org.lwjgl.glfw.GLFW;

import toast.client.gui.clickgui.ClickGuiScreen;
import toast.client.gui.clickgui.SettingSlider;
import toast.client.module.Category;
import toast.client.module.Module;

public class ClickGui extends Module {
	
	public static ClickGuiScreen clickGui = new ClickGuiScreen();
	
	public ClickGui() {
		super("ClickGui", GLFW.GLFW_KEY_RIGHT_SHIFT, Category.RENDER, "Opens ClickGUI!",
				new SettingSlider("Length: ", 70, 100, 80, 0));
	}
	
	public void onEnable() {
		mc.openScreen(clickGui);
		setToggled(false);
	}

}
