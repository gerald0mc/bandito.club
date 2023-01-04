package club.bandito.client.gui.settings;

import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.Component;
import club.bandito.client.gui.SettingComponent;
import club.bandito.client.module.setting.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ListComponent extends SettingComponent {
    public ListSetting setting;
    private boolean open = false;
    private List<Component> settingComps = new LinkedList<>();

    public ListComponent(ListSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        int yOffset = 0;
        for (Setting s : setting.getList()) {
            if (s instanceof BooleanSetting) {
                settingComps.add(new BooleanComponent((BooleanSetting) s, x, y + yOffset, width, height));
            } else if (s instanceof FloatSetting) {
                settingComps.add(new FloatComponent((FloatSetting) s, x, y + yOffset, width, height));
            } else if (s instanceof ModeSetting) {
                settingComps.add(new ModeComponent((ModeSetting) s, x, y + yOffset, width, height));
            } else if (s instanceof PositionSetting) {
                settingComps.add(new PositionComponent((PositionSetting) s, x, y + yOffset, width, height));
            }
            yOffset += height;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                open = !open;
            }
        }
        if (open) {
            for (Component component : settingComps) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (Component component : settingComps) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x80000000);
        Gui.drawRect(x, y, x + 1, y + height, ClickGuiScreen.guiColor.getRGB());
        Gui.drawRect(x + width - 1, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());
        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        fr.drawStringWithShadow(trimValue("", (open ? ChatFormatting.WHITE + "> " + ChatFormatting.RESET : "") + (isInside(mouseX, mouseY) ? ChatFormatting.WHITE : "") + setting.getName(), "", width, 8), x + 4f, y + 2f, new Color(140, 140, 140, 255).getRGB());
        if (open) {
            int yOffset = height;
            for(Component component : settingComps) {
                component.x = x;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawComponent(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }

    @Override
    public int getHeight() {
        if (open) {
            return height + (settingComps.size() * height);
        } else {
            return height;
        }
    }
}
