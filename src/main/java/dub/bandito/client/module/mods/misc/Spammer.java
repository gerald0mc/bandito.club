package club.bandito.client.module.mods.misc;

import club.bandito.client.Bandito;
import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.module.setting.ModeSetting;
import club.bandito.client.util.MessageUtil;
import club.bandito.client.util.Timer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Spammer extends Module {
    private final ModeSetting readMode = new ModeSetting("ReadMode", "LineByLine", "LineByLine", "Random");
    private final FloatSetting delay = new FloatSetting("Delay(Secs)", 5, 1, 20);
    private final BooleanSetting randomNumbers = new BooleanSetting("RandomNumbers", true);

    private final Random random = new Random();
    private final Timer timer = new Timer();
    private final List<String> spamMessages;
    private final File spammerFile;
    private int line = 0;
    private int antiResend = 0;

    public Spammer() {
        super("Spammer", "Spams in chat for you.", Category.MISC);
        spamMessages = new LinkedList<>();
        spammerFile = new File(Bandito.getConfigManager().getMiscDir(), "Spammer.txt");
        try {
            if (!spammerFile.exists()) {
                spammerFile.createNewFile();
                System.out.println("[Bandito] Created Spammer.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(spammerFile));
                //Default message
                writer.write("Bandito is the best client ever made on god!");
                writer.flush();
                writer.close();
            }
            reloadSpammer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        if (spamMessages.isEmpty()) return;
        if (!timer.passed((long) (delay.getValue() * 1000))) return;
        switch (readMode.getMode()) {
            case "LineByLine":
                if (line == spamMessages.size()) line = 0;
                mc.player.sendChatMessage(spamMessages.get(line) + (randomNumbers.getValue() ? " " + RandomStringUtils.random(5, true, true) : ""));
                line++;
                timer.reset();
                break;
            case "Random":
                int nextInt = random.nextInt(spamMessages.size() - 1);
                if (nextInt == antiResend && spamMessages.size() != 1) return;
                antiResend = nextInt;
                String message = spamMessages.get(antiResend) + (randomNumbers.getValue() ? " " + RandomStringUtils.random(5, true, true) : "");
                mc.player.sendChatMessage(message);
                timer.reset();
                break;
        }
    }

    public void reloadSpammer() {
        try {
            if (!spammerFile.exists()) return;
            spamMessages.clear();
            BufferedReader reader = new BufferedReader(new FileReader(spammerFile));
            String line = reader.readLine();
            while (line != null) {
                spamMessages.add(line);
                System.out.println("[Bandito] " + line + " ADDED TO SPAMMER!");
                line = reader.readLine();
            }
            reader.close();
            if (!nullCheck()) {
                MessageUtil.sendMessage("Reloaded spammer with " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + spamMessages.size() + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "messages!", false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
