package club.bandito.client.module.mods.hud.mods.coords;

import club.bandito.client.module.mods.hud.HUDModule;

public class Coordinates extends HUDModule {
    public Coordinates() {
        super(new CoordinatesComponent(0, mc.displayHeight - mc.fontRenderer.FONT_HEIGHT), "Coordinates", "Renders your coords.", Category.HUD);
    }
}
