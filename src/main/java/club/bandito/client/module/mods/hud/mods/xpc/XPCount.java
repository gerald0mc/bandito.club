package club.bandito.client.module.mods.hud.mods.xpc;

import club.bandito.client.module.mods.hud.HUDModule;

public class XPCount extends HUDModule {
    public XPCount() {
        super(new XPCountComponent(0, 303), "XPCount", "Shows how many xp bottles you have atm.", Category.HUD);
    }
}
