package club.bandito.client.util;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.client.Messages;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

import java.util.*;

public class MessageUtil {
    private static final String PREFIX = ChatFormatting.GRAY + "[" + ChatFormatting.RESET + "bandito" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET;

    public static void sendMessage(String message, boolean raw) {
        final Messages module = Bandito.getModuleManager().getModule(Messages.class);
        switch (module.messageMode.getMode()) {
            case "Chat":
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString((raw ? "" : PREFIX + ChatFormatting.RESET) + message));
                break;
            case "Notification":
                Bandito.getNotificationManager().addNotification(message);
                break;
            case "Both":
                Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString((raw ? "" : PREFIX + ChatFormatting.RESET) + message));
                Bandito.getNotificationManager().addNotification(message);
        }
    }

    public static void sendMessage(String message, boolean raw, int id) {
        final Messages module = Bandito.getModuleManager().getModule(Messages.class);
        switch (module.messageMode.getMode()) {
            case "Chat":
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString((raw ? "" : PREFIX + ChatFormatting.RESET) + message), id);
                break;
            case "Notification":
                Bandito.getNotificationManager().addNotification(message);
                break;
            case "Both":
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString((raw ? "" : PREFIX + ChatFormatting.RESET) + message), id);
                Bandito.getNotificationManager().addNotification(message);
        }
    }

    public static String getLongestWordString(LinkedList<String> list) {
        String longestWord = null;
        for (String s : list) {
            if (longestWord == null || Minecraft.getMinecraft().fontRenderer.getStringWidth(s) > Minecraft.getMinecraft().fontRenderer.getStringWidth(longestWord)) {
                longestWord = s;
            }
        }
        return longestWord;
    }

    public static int getLongestWordInt(LinkedList<String> list) {
        int longestWordInt = -1;
        for (String s : list) {
            if (longestWordInt == -1 || Minecraft.getMinecraft().fontRenderer.getStringWidth(s) > longestWordInt) {
                longestWordInt = Minecraft.getMinecraft().fontRenderer.getStringWidth(s);
            }
        }
        return longestWordInt;
    }
}
