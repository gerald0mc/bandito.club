package club.bandito.client.module.mods.hud.mods.arraylist;

import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.module.setting.BooleanSetting;

public class ArrayList extends HUDModule {
    public ArrayList() {
        super(new ArrayListComponent(0, mc.fontRenderer.FONT_HEIGHT + 2), "ArrayList", "Shows enabled modules.", Category.HUD);
    }

    public final BooleanSetting skipHUD = new BooleanSetting("SkipHUD", true);
}
