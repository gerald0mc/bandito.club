package club.bandito.client.module.mods.hud.mods.crystalc;

import club.bandito.client.module.mods.hud.HUDModule;

public class CrystalCount extends HUDModule {
    public CrystalCount() {
        super(new CrystalCountComponent(0, 269), "CrystalCount", "Shows how many crystals you have atm.", Category.HUD);
    }
}
