package club.bandito.client.module.mods.misc;

import club.bandito.client.module.Module;
import club.bandito.client.module.mods.combat.PopLagger;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.util.MessageUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiLaggerText extends Module {
    public AntiLaggerText() {
        super("AntiLaggerText", "Stops you from seeing lag messages.", Category.MISC);
    }

    private final BooleanSetting message = new BooleanSetting("Message", false);

    @SubscribeEvent
    public void onClientR(ServerChatEvent event) {
        if (event.getMessage().contains(PopLagger.lagText)) {
            if (!event.isCanceled()) {
                event.setCanceled(true);
                if (message.getValue()) MessageUtil.sendMessage("Canceled you from seeing lag text.", false);
            }
        }
    }
}
