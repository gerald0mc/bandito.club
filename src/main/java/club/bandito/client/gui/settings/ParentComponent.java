package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.Component;
import club.bandito.client.module.setting.ParentSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ParentComponent extends Component {
    private final ParentSetting setting;

    public ParentComponent(ParentSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            setting.setValue(!setting.getValue());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
        String str = ((setting.getValue() ? ChatFormatting.WHITE + "> " : (isInside(mouseX, mouseY) ? ChatFormatting.WHITE : "")) + setting.getName());
        fr.drawStringWithShadow(str, x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }

    @Override
    public int getHeight() {
        return height;
    }
}
