package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.SettingComponent;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.MathUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class FloatComponent extends SettingComponent {

    private final FloatSetting setting;
    private boolean dragging = false;

    public FloatComponent(FloatSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && dragging) {
            dragging = false;
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());

        if (dragging) {
            final int min = x + 2;
            final int max = x + width - 2;
            final float value = MathUtil.normalize(mouseX, min, max) * setting.getMax();
            if (!isInside(mouseX, mouseY)) {
                dragging = false;
                setting.setValue(setting.getMin());
            }
            int diff = mouseX - min;
            if (diff <= 0) {
                setting.setValue(setting.getMin());
            } else {
                setting.setValue(value);
            }
        }

        float sliderWidth = MathUtil.normalize(setting.getValue(), setting.getMin(), setting.getMax()) * (width - 4);
        Gui.drawRect(x + 2, y + (height / 2) + 4, x + 2 + (int) sliderWidth, y + (height / 2) + 5, new Color(ClickGuiScreen.guiColor.getRed(), ClickGuiScreen.guiColor.getGreen(), ClickGuiScreen.guiColor.getBlue()).getRGB());
        String str = ChatFormatting.WHITE + String.valueOf(setting.getValue());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((isInside(mouseX, mouseY) ? ChatFormatting.WHITE : ChatFormatting.RESET) + trimValue("", setting.getName(), str, width, 8), x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(str, x + width - 4f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(str)), y + 2f, new Color(140, 140, 140, 255).getRGB());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }

    @Override
    public int getHeight() {
        return height;
    }

    public FloatSetting getSetting() {
        return setting;
    }
}
