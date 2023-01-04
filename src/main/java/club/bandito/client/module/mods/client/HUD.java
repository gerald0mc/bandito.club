package club.bandito.client.module.mods.client;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.module.setting.ListSetting;
import club.bandito.client.module.setting.Setting;

public class HUD extends Module {
    public HUD() {
        super("HUD", "Module for HUD stuff.", Category.CLIENT);
        setAlwaysEnabled(true);
    }

    public final BooleanSetting clientSync = new BooleanSetting("ClientSync", true);
    public final ListSetting color = new ListSetting("Color", new Setting[] {
            new FloatSetting("Red", 87, 0, 255),
            new FloatSetting("Green", 60, 0, 255),
            new FloatSetting("Blue", 209, 0, 255)
    });
}
