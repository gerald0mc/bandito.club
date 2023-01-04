package club.bandito.client.module.mods.hud;

import club.bandito.client.module.Module;

public abstract class HUDModule extends Module {
    private final HUDComponent component;

    public HUDModule(HUDComponent component, String name, String description, Category category) {
        super(name, description, category);
        this.component = component;
        this.component.motherModule = this;
    }

    public HUDComponent getComponent() {
        return component;
    }
}
