package club.bandito.client.gui;

import club.bandito.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModulePanel extends Component {

    private final Module.Category category;
    private final List<Component> components;

    private int dragX;
    private int dragY;
    private boolean dragging;

    public ModulePanel(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.components = new CopyOnWriteArrayList<>();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }

        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (dragging && mouseButton == 0) {
            dragging = false;
        }

        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        Gui.drawRect(x, y, x + width, y + height, ClickGuiScreen.guiColor.getRGB());

        final String title = category.toString() + " (" + components.size() + ")";
        final float middle = x + (width / 2f) - (Minecraft.getMinecraft().fontRenderer.getStringWidth(title) / 2f);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(title, middle,
                y + (height / 2f) - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2f) + 1f, -1);

        int compY = y + height;
        for (Component component : components) {
            component.x = x;
            component.y = compY;
            component.drawComponent(mouseX, mouseY, partialTicks);
            compY += component.getHeight();
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        for (Component component : components) {
            component.keyTyped(keyChar, keyCode);
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Module.Category getCategory() {
        return category;
    }

    public List<Component> getComponents() {
        return components;
    }
}
