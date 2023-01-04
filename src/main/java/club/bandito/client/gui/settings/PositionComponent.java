package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.SettingComponent;
import club.bandito.client.module.setting.PositionSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class PositionComponent extends SettingComponent {

    private final PositionSetting setting;

    public PositionComponent(PositionSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (isInside(mouseX, mouseY)) {
            if (mc.player == null)
                return;

            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (mouseButton == 0) {
                setting.setPosition(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
            } else if (mouseButton == 1) {
                setting.setPosition(null);
            }
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
        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        String str = (setting.getPosition() != null ? ChatFormatting.WHITE : ChatFormatting.RESET) + (setting.getPosition() == null ? "Not Set" : setting.getPosition().getX() + " " + setting.getPosition().getY() + " " + setting.getPosition().getZ());
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
