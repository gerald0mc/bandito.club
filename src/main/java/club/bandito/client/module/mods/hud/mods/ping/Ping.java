package club.bandito.client.module.mods.hud.mods.ping;

import club.bandito.client.module.mods.hud.HUDModule;

public class Ping extends HUDModule {
    public Ping() {
        super(new PingComponent(0, 0), "Ping", "Renders your ping in ms.", Category.HUD);
    }
}
