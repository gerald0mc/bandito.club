package club.bandito.client.module.mods.render;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;

public class NoRender extends Module {
    public NoRender() {
        super("NoRender", "Stops rendering various things.", Category.RENDER);
    }

    public final BooleanSetting particles = new BooleanSetting("Particles", true);
    public final BooleanSetting totemPops = new BooleanSetting("TotemPops", true);
}
