package club.bandito.client.module.mods.hud.mods.notifications;

import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.module.setting.FloatSetting;

public class Notifications extends HUDModule {
    public Notifications() {
        super(new NotificationsComponent(0, 400), "Notifications", "Renders notifications from the client.", Category.HUD);
    }

    public final FloatSetting timeActive = new FloatSetting("TimeActive(Secs)", 5, 1, 20);
}
