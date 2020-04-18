package toast.client.module;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import toast.client.ToastClient;
import toast.client.gui.clickgui.SettingBase;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.MinecraftClient;

public class Module {

	protected MinecraftClient mc = MinecraftClient.getInstance();
	private String name;
	private int key;
	private boolean toggled;
	private Category category;
	private String desc;
	private List<SettingBase> settings = new ArrayList<>();
	
	public Module(String name, int key, Category category, String description, SettingBase... settings) {
		this.name = name;
		setKey(key);
		this.category = category;
		desc = description;
		this.settings = Arrays.asList(settings);
		toggled = false;
	}
	
	
	public void toggle() {
		toggled = !toggled;
		if(toggled) onEnable();
		else onDisable();
	}
	
	public void onEnable() {
		for(Method method : getClass().getMethods()) {
			if (method.isAnnotationPresent(Subscribe.class)) {
				ToastClient.eventBus.register(this);
				break;
			}
		}
	}

	public void onDisable() {
		try{
			for(Method method : getClass().getMethods()) {
				if (method.isAnnotationPresent(Subscribe.class)) {
					ToastClient.eventBus.unregister(this);
					break;
				}
			}
		}catch(Exception this_didnt_get_registered_hmm_weird) { this_didnt_get_registered_hmm_weird.printStackTrace(); } 
	}

	public String getName() {
		return name;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKey() {
		return key;
	}
	
	public List<SettingBase> getSettings() {
		return settings;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		if(toggled) onEnable();
		else onDisable();
	}
	
}
