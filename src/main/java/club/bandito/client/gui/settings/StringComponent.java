package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.SettingComponent;
import club.bandito.client.module.setting.StringSetting;
import club.bandito.client.util.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StringComponent extends SettingComponent {
    private final StringSetting setting;
    private String entryText = "";
    private boolean isTyping = false;

    public StringComponent(StringSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (isTyping) {
                if (!entryText.equals("")) {
                    setting.setValue(entryText);
                }
            }
            isTyping = !isTyping;
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
        String str = (!isTyping ? ChatFormatting.WHITE + setting.getValue() : "");
        fr.drawStringWithShadow(trimValue("", (isTyping ? ChatFormatting.WHITE + entryText : (isInside(mouseX, mouseY) ? ChatFormatting.WHITE : ChatFormatting.RESET) + setting.getName()), str, width, 8), x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
        fr.drawStringWithShadow(str, x + width - 4f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(str)), y + 2f, new Color(140, 140, 140, 255).getRGB());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        if (isTyping) {
            switch (keyCode) {
                case Keyboard.KEY_BACK:
                    entryText = entryText.substring(0, entryText.length() - 1);
                    break;
                case Keyboard.KEY_RETURN:
                    if (entryText.length() > 0) {
                        setting.setValue(entryText);
                    }
                    isTyping = false;
                    entryText = "";
                    break;
                case Keyboard.KEY_V:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        try {
                            entryText += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                        } catch (UnsupportedFlavorException | IOException ignored) { }
                    }
                    break;
                case Keyboard.KEY_C:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        if (entryText.length() == 0) {
                            MessageUtil.sendMessage("Nothing to copy.", false);
                            return;
                        }
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(entryText), null);
                        MessageUtil.sendMessage("Copied text box string to paste board.", false);
                    }
                    break;
            }
            if (ChatAllowedCharacters.isAllowedCharacter(keyChar))
                entryText += keyChar;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
