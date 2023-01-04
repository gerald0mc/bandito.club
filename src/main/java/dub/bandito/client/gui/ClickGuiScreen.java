package club.bandito.client.gui;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.module.mods.client.ClickGui;
import club.bandito.client.module.setting.FloatSetting;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {

    private final List<Component> components;
    public static Color guiColor = Color.MAGENTA;

    public ClickGuiScreen() {
        this.components = new ArrayList<>();

        int x = 2;
        for (Module.Category category : Module.Category.values()) {
            final ModulePanel panel = new ModulePanel(category, x, 2, 110, 14);
            for (Module module : Bandito.getModuleManager().getModules(category)) {
                if (module.getCategory() == Module.Category.HUD) {
                    HUDModule hudModule = (HUDModule) module;
                    components.add(hudModule.getComponent());
                }
                panel.getComponents().add(new ModuleButton(module, 0, 0, panel.width, 14));
            }

            components.add(panel);
            x += panel.width + 2;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        components.forEach(component -> component.drawComponent(mouseX, mouseY, partialTicks));
        ClickGui module = Bandito.getModuleManager().getModule(ClickGui.class);
        guiColor = new Color((int) module.red.getValue(), (int) module.green.getValue(), (int) module.blue.getValue(), (int) module.alpha.getValue());
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) throws IOException {
        super.keyTyped(keyChar, keyCode);
        components.forEach(component -> component.keyTyped(keyChar, keyCode));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public List<Component> getComponents() {
        return components;
    }
}
