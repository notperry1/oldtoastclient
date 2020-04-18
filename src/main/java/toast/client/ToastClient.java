package toast.client;

import com.google.common.eventbus.EventBus;

import toast.client.module.ModuleManager;
import toast.client.module.mods.ClickGui;
import toast.client.utils.file.ToastFileHelper;
import toast.client.utils.file.ToastFileMang;
import net.fabricmc.api.ModInitializer;

public class ToastClient implements ModInitializer {
	
	public static String VERSION = "1.0.0";
	public static int INTVERSION = 1;
	public static EventBus eventBus;

	@Override
	public void onInitialize() {
		eventBus = new EventBus();
		
		ToastFileMang.init();
		ToastFileHelper.readModules();
    	ToastFileHelper.readSettings();
    	ToastFileHelper.readBinds();
    	
    	ClickGui.clickGui.initWindows();
    	ToastFileHelper.readClickGui();
    	ToastFileHelper.readPrefix();

    	eventBus.register(new ModuleManager());
	}
}
