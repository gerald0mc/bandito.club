package club.bandito.client.module.mods.client;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.*;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", "Opens the gui menu.", Category.CLIENT);
        setKey(Keyboard.KEY_U);
    }

    public final ParentSetting colorParent = new ParentSetting("Color");
    public final FloatSetting red = new FloatSetting("Red", 87, 0, 255, colorParent);
    public final FloatSetting green = new FloatSetting("Green", 60, 0, 255, colorParent);
    public final FloatSetting blue = new FloatSetting("Blue", 209, 0, 255, colorParent);
    public final FloatSetting alpha = new FloatSetting("Alpha", 255, 0, 255, colorParent);

    @Override
    protected void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        mc.displayGuiScreen(Bandito.getClickGui());
        disable();
    }
}
