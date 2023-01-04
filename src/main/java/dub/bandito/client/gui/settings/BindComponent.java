package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.Component;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.ParentSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class BindComponent extends Component {
    public Module module;
    public boolean listening = false;
    public final ParentSetting parent;

    public BindComponent(Module module, int x, int y, int width, int height, ParentSetting parent) {
        super(x, y, width, height);
        this.module = module;
        this.parent = parent;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                listening = !listening;
            } else if (mouseButton == 1) {
                if (listening) listening = false;
                module.setKey(Keyboard.KEY_NONE);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);

        String str = (module.getKey() != Keyboard.KEY_NONE ? ChatFormatting.WHITE : ChatFormatting.RESET) + Keyboard.getKeyName(module.getKey());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((isInside(mouseX, mouseY) ? ChatFormatting.WHITE : ChatFormatting.RESET) + trimValue("", listening ? "Listening..." : "Bind", str, width, 8), x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(str, x + width - 4f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(str)), y + 2f, new Color(140, 140, 140, 255).getRGB());

        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        if (listening) {
            module.setKey(keyCode);
            listening = false;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
