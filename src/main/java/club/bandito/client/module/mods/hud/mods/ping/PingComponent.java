package club.bandito.client.module.mods.hud.mods.ping;

import club.bandito.client.module.mods.hud.HUDComponent;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;

public class PingComponent extends HUDComponent {
    public PingComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        String string = "Ping " + ChatFormatting.WHITE + getPing();
        this.width = mc.fontRenderer.getStringWidth(string);
        mc.fontRenderer.drawStringWithShadow(string, x, y, color.getRGB());
    }

    @Override
    public int getHeight() {
        return height;
    }

    public String getPing() {
        String ping;
        try {
            ping = "" + Minecraft.getMinecraft().getConnection().getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime();
        } catch (Exception e) {
            return "?";
        }
        return ping;
    }
}
