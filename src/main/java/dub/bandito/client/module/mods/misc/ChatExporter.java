package club.bandito.client.module.mods.misc;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import club.bandito.client.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ChatExporter extends Module {
    public ChatExporter() {
        super("ChatExporter", "Exports your chats into a txt.", Category.MISC);
    }

    private BufferedWriter fileWriter = null;
    private boolean inServer = false;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (mc.world == null && mc.player == null && inServer) {
            inServer = false;
            try {
                fileWriter.flush();
                fileWriter.close();
                fileWriter = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onChatR(ClientChatReceivedEvent event) {
        if (fileWriter != null) {
            String time = new SimpleDateFormat("k:mm").format(new Date());
            try {
                fileWriter.write("[" + time + "] " + event.getMessage().getUnformattedText() + "\n");
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() == mc.player) {
            if (inServer) {
                System.out.println("[Bandito] Already in server not setting inServer.");
                return;
            }
            inServer = true;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String string = (Minecraft.getMinecraft().getCurrentServerData() == null ? "Singleplayer" : Minecraft.getMinecraft().getCurrentServerData().serverIP) + "_" + dtf.format(now)
                    .replace(" ", "_")
                    .replace(":", "_")
                    .replace("/", "_") + ".txt";
            System.out.println(string);
            File chatFile = new File(Bandito.getConfigManager().getExportedChatFile(), string);
            try {
                if (!chatFile.exists()) {
                    chatFile.createNewFile();
                    if (!Module.nullCheck()) MessageUtil.sendMessage("Created new chat log txt.", false);
                    System.out.println("[Bandito] Created new chat log txt.");
                }
                fileWriter = new BufferedWriter(new FileWriter(chatFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
