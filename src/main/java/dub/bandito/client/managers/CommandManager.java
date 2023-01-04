package club.bandito.client.managers;

import club.bandito.client.command.Command;
import club.bandito.client.command.commands.*;
import com.google.common.reflect.ClassPath;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class CommandManager {
    public List<Command> commandList;
    public String PREFIX = "+";

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
        commandList = new LinkedList<>();
        loadCommands("club.bandito.client.command.commands");
    }

    public List<Command> getCommands() {
        return commandList;
    }

    public void setPREFIX(int keyboardKey) {
        this.PREFIX = String.valueOf((char) keyboardKey);
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(PREFIX)) {
            for (Command command : getCommands()) {
                if (args[0].equalsIgnoreCase((PREFIX + command.getUsage()[0]))) {
                    event.setCanceled(true);
                    command.onCommand(args);
                    return;
                }
            }
        }
    }

    @SuppressWarnings({"UnstableApiUsage", "SameParameterValue"})
    private void loadCommands(String pkg) {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(Launch.classLoader).getAllClasses()) {
                if (classInfo.getName().startsWith(pkg)) {
                    final Class<?> clazz = classInfo.load();

                    if (!Modifier.isAbstract(clazz.getModifiers()) && Command.class.isAssignableFrom(clazz)) {
                        for (Constructor<?> constructor : clazz.getConstructors()) {
                            if (!constructor.isAccessible()) {
                                constructor.setAccessible(true);
                            }

                            if (constructor.getParameterCount() == 0) {
                                final Command command = (Command) constructor.newInstance();
                                for (Field field : command.getClass().getDeclaredFields()) {
                                    if (!field.isAccessible())
                                        field.setAccessible(true);
                                }
                                commandList.add(command);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
