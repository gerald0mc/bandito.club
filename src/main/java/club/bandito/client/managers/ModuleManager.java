package club.bandito.client.managers;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.Setting;
import club.bandito.client.util.MessageUtil;
import com.google.common.reflect.ClassPath;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules;
    private final Map<String, Module> nameMap;
    public final Map<Class<? extends Module>, Module> classMap;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        this.nameMap = new HashMap<>();
        this.classMap = new HashMap<>();
        loadModules("club.bandito.client.module.mods");
        loadModules("club.bandito.client.module.hud.mods");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void registerModule(Module module) {
        for (Field field : module.getClass().getDeclaredFields()) {
            if (!field.isAccessible())
                field.setAccessible(true);
            if (Setting.class.isAssignableFrom(field.getType())) {
                try {
                    final Setting setting = (Setting) field.get(module);
                    if (setting == null)
                        continue;
                    module.getSettings().add(setting);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        modules.add(module);
        nameMap.put(module.getName(), module);
        classMap.put(module.getClass(), module);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadModules(String pkg) {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(Launch.classLoader).getAllClasses()) {
                if (classInfo.getName().startsWith(pkg)) {
                    final Class<?> clazz = classInfo.load();
                    if (!Modifier.isAbstract(clazz.getModifiers()) && Module.class.isAssignableFrom(clazz)) {
                        for (Constructor<?> constructor : clazz.getConstructors()) {
                            if (!constructor.isAccessible()) constructor.setAccessible(true);
                            if (constructor.getParameterCount() == 0) {
                                final Module module = (Module) constructor.newInstance();
                                registerModule(module);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModule(String name) {
        return nameMap.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) classMap.get(clazz);
    }

    public List<Module> getModules(Module.Category category) {
        return modules.stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != 0) {
            for (Module module : modules) {
                if (module.getKey() == Keyboard.getEventKey()) {
                    if (module.isAlwaysEnabled()) {
                        MessageUtil.sendMessage(module.getName() + " is a permanently enabled module!", false);
                        return;
                    }
                    module.toggle();
                    return;
                }
            }
        }
    }
}
