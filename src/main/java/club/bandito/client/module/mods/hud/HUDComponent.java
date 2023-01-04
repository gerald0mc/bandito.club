package club.bandito.client.module.mods.hud;

import club.bandito.client.Bandito;
import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.gui.Component;
import club.bandito.client.module.mods.client.HUD;
import club.bandito.client.module.setting.FloatSetting;
import net.minecraft.client.Minecraft;

import java.awt.*;

public abstract class HUDComponent extends Component {
    public int x;
    public int y;
    public int width = 10;
    public int height = mc.fontRenderer.FONT_HEIGHT;
    public HUDModule motherModule = null;
    public boolean dragging = false;
    public Color color = Color.WHITE;

    public static Minecraft mc = Minecraft.getMinecraft();
    public int mouseX;
    public int mouseY;

    public HUDComponent(int x, int y) {
        super(x, y, 0, mc.fontRenderer.FONT_HEIGHT);
    }

    public void doRender() { }

    public boolean isInside(int posX, int posY) {
        return posX > x && posY > y && posX < x + width && posY < y + height;
    }

    public boolean isInside() {
        return mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && motherModule.isEnabled())
            if (mouseButton == 0)
                dragging = !dragging;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && motherModule.isEnabled())
            if (dragging)
                dragging = false;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        HUD module = Bandito.getModuleManager().getModule(HUD.class);
        Color hudColor = new Color((int) ((FloatSetting) module.color.getList().get(0)).getValue(), (int) ((FloatSetting) module.color.getList().get(1)).getValue(), (int) ((FloatSetting) module.color.getList().get(2)).getValue());
        if (color != ClickGuiScreen.guiColor && module.clientSync.getValue()) {
            color = ClickGuiScreen.guiColor;
        } else if (color != hudColor && !module.clientSync.getValue())
            color = hudColor;
        if (motherModule.isEnabled()) {
            if (dragging) {
                this.x = mouseX;
                this.y = mouseY;
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }
}
