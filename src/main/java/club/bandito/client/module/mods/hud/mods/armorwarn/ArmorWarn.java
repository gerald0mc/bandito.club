package club.bandito.client.module.mods.hud.mods.armorwarn;

import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.module.setting.FloatSetting;

public class ArmorWarn extends HUDModule {
    public ArmorWarn() {
        super(new ArmorWarnComponent(0, 0), "ArmorWarn", "Worns you when your armor pieces are low dura.", Category.HUD);
    }

    public final FloatSetting warnDura = new FloatSetting("WarnDura", 25, 1, 99);
}
