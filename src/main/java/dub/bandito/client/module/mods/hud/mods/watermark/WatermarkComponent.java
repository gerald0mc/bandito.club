package club.bandito.client.module.mods.hud.mods.watermark;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.module.mods.hud.HUDModule;
import com.mojang.realmsclient.gui.ChatFormatting;

public class WatermarkComponent extends HUDComponent {
    public WatermarkComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        Watermark module = Bandito.getModuleManager().getModule(Watermark.class);
        String string = (module.custom.getValue() ? module.customString.getValue() : Bandito.MOD_NAME) + (module.version.getValue() ? " " + ChatFormatting.WHITE + Bandito.VERSION : "");
        this.width = mc.fontRenderer.getStringWidth(string);
        mc.fontRenderer.drawStringWithShadow(string, x, y, color.getRGB());
    }

    @Override
    public int getHeight() {
        return height;
    }
}
