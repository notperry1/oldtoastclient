package toast.client.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import toast.client.event.events.EventKeyPress;
import com.google.common.eventbus.Subscribe;
import toast.client.module.mods.hidden.Teleport;
import toast.client.module.mods.misc.Peek;
import toast.client.module.mods.player.Nofall;
import toast.client.module.mods.render.ClickGui;
import toast.client.module.mods.misc.AutoReconnect;
import toast.client.module.mods.render.HUD;
import toast.client.module.mods.render.Xray;
import toast.client.module.mods.world.ChunkSize;
import toast.client.module.mods.world.Scaffold;

public class ModuleManager {

	private static List<Module> mods = Arrays.asList(
			new AutoReconnect(),
			new ChunkSize(),
			new ClickGui(),
			new Nofall(),
			new Scaffold(),
			new Teleport(),
			new Xray(),
			new Peek(),
			new HUD());
	
	public static List<Module> getModules() {
		return mods;
	}

	public static Module getModule(Class<? extends Module> clazz) {
		for(Module module : mods) {
			if(module.getClass().equals(clazz)) {
				return module;
			}
		}

		return null;
	}

	public static Module getModuleByName(String name) {
	    for (Module m: mods) {
	        if (name.equalsIgnoreCase(m.getName())) return m;
	    }
	    return null;
	}
	
	public static List<Module> getModulesInCat(Category cat) {
		List<Module> mds = new ArrayList<>();
	    for (Module m: mods) {
	        if (m.getCategory().equals(cat)) mds.add(m);
	    }
	    return mds;
	}

	@Subscribe
	public static void handleKeyPress(EventKeyPress eventKeyPress) {
		for (Module m : mods) {
			if (m.getKey() == eventKeyPress.getKey()) {
				m.toggle();
			}
		}
	}
}
