package toast.client.utils;

import toast.client.module.Category;
import toast.client.module.Module;
import toast.client.module.ModuleManager;

import java.util.Collections;
import java.util.List;

public class CategoryUtils {
    public static List<Module> getModulesInCategory(Category category) {
        List<Module> matches = new java.util.ArrayList<>(Collections.emptyList());
        for (Module module : ModuleManager.getModules()) {
             if (module.getCategory() == category) {
                 matches.add(module);
             }
        }
        return matches;
    }
}
