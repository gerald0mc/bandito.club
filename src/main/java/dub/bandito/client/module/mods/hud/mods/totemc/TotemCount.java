package club.bandito.client.module.mods.hud.mods.totemc;

import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.module.mods.hud.HUDModule;
import net.minecraft.client.gui.ScaledResolution;

public class TotemCount extends HUDModule {
        public TotemCount() {
                super(new TotemCountComponent(0, 250), "TotemCount", "Shows how many totems you have atm.", Category.HUD);
        }
}
