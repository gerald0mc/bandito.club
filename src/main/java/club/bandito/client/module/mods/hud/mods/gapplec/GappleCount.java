package club.bandito.client.module.mods.hud.mods.gapplec;

import club.bandito.client.module.mods.hud.HUDModule;

public class GappleCount extends HUDModule {
    public GappleCount() {
        super(new GappleCountComponent(0, 286), "GappleCount", "Shows how many gapples you have atm.", Category.HUD);
    }
}
