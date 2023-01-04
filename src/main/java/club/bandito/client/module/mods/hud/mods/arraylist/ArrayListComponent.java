package club.bandito.client.module.mods.hud.mods.arraylist;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;

import java.util.LinkedList;

public class ArrayListComponent extends HUDComponent {
    public ArrayListComponent(int x, int y) {
        super(x, y);
    }

    private ArrayList module;

    @Override
    public void doRender() {
        int yOffset = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        module = Bandito.getModuleManager().getModule(ArrayList.class);
        LinkedList<String> modNames = new LinkedList<>();
        for (Module m : Bandito.getModuleManager().getModules()) {
            if (m.getCategory() == Module.Category.HUD && module.skipHUD.getValue()) continue;
            if (m.getCategory() == Module.Category.CLIENT) continue;
            if (!m.isDrawn()) continue;
            if (m.isEnabled()) {
                int thing = sr.getScaledWidth() / 2;
                String string = m.getName() + (m.getMetaData() != null ? ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + m.getMetaData() + ChatFormatting.GRAY + "]" : "");
                modNames.add(string);
                mc.fontRenderer.drawStringWithShadow(string, x > thing ? x + width - mc.fontRenderer.getStringWidth(string) : x, y + yOffset, color.getRGB());
                yOffset += mc.fontRenderer.FONT_HEIGHT;
            }
        }
        this.width = MessageUtil.getLongestWordInt(modNames);
        this.height = yOffset == 0 ? mc.fontRenderer.FONT_HEIGHT : yOffset;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
