package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.Component;
import club.bandito.client.gui.SettingComponent;
import club.bandito.client.module.setting.ModeSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeComponent extends SettingComponent {
    private final ModeSetting setting;

    public ModeComponent(ModeSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                setting.decrement();
            } else if (mouseButton == 1) {
                setting.increment();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        String str = ChatFormatting.WHITE + setting.getMode();
        fr.drawStringWithShadow((isInside(mouseX, mouseY) ? ChatFormatting.WHITE : ChatFormatting.RESET) + trimValue("", setting.getName(), str, width, 8), x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
        fr.drawStringWithShadow(str, x + width - 4f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(str)), y + 2f, new Color(140, 140, 140, 255).getRGB());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }

    @Override
    public int getHeight() {
        return height;
    }
}
