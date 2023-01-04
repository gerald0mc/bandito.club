package club.bandito.client.module.mods.hud.mods.watermark;

import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.StringSetting;

public class Watermark extends HUDModule {
    public Watermark() {
        super(new WatermarkComponent(0, 0), "Watermark", "Flex Bandito on all the haters.", Category.HUD);
    }

    public final BooleanSetting version = new BooleanSetting("Version", true);
    public final BooleanSetting custom = new BooleanSetting("Custom", false);
    public final StringSetting customString = new StringSetting("CustomString", "FaggotHack");
}
