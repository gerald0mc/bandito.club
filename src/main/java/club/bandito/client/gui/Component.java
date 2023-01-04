package club.bandito.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;

import java.awt.*;

public abstract class Component extends Rect {
    public Component(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

    public abstract void drawComponent(int mouseX, int mouseY, float partialTicks);

    public abstract void keyTyped(char keyChar, int keyCode);

    public abstract int getHeight();

    public String trimValue(String preValue, String value, String otherValue, int width, int amountIn) {
        int otherWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(otherValue);
        int maxWidth = width - amountIn - otherWidth;
        if (Minecraft.getMinecraft().fontRenderer.getStringWidth(value) < maxWidth) return preValue + value;
        else {
            int preWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(preValue);
            int dotWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth("...");
            return preValue + Minecraft.getMinecraft().fontRenderer.trimStringToWidth(value, maxWidth - preWidth - dotWidth) + "...";
        }
    }
}
