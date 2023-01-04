package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.SettingComponent;
import club.bandito.client.module.setting.BooleanSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class BooleanComponent extends SettingComponent {

    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            setting.toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
        String str = (setting.getValue() ? ChatFormatting.WHITE : ChatFormatting.RESET) + (setting.getValue() ? "True" : "False");
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((isInside(mouseX, mouseY) ? ChatFormatting.WHITE : ChatFormatting.RESET) + trimValue("", setting.getName(), str, width, 8), x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(str, x + width - 4f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(str)), y + 2f, new Color(140, 140, 140, 255).getRGB());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }

    @Override
    public int getHeight() {
        return height;
    }

    public BooleanSetting getSetting() {
        return setting;
    }
}
