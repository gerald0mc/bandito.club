package club.bandito.client.module.mods.client;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.ModeSetting;

public class Messages extends Module {
    public Messages() {
        super("Messages", "Settings for client messages.", Category.CLIENT);
        setAlwaysEnabled(true);
    }

    public final ModeSetting messageMode = new ModeSetting("MessageMode", "Both", "Chat", "Notification", "Both");
}
