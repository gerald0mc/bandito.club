package club.bandito.client.module.mods.hud.mods.armorwarn;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.LinkedList;

public class ArmorWarnComponent extends HUDComponent {
    public ArmorWarnComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        int yOffset = 0;
        ArmorWarn module = Bandito.getModuleManager().getModule(ArmorWarn.class);
        LinkedList<String> strings = new LinkedList<>();
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            if (stack.getItem().equals(Items.AIR)) continue;
            float dura = ((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage();
            String stackName = stack.getItem().getItemStackDisplayName(new ItemStack(stack.getItem()));
            if (dura <= module.warnDura.getValue()) {
                String string = "WARNING! Your " + stackName + (stackName.endsWith("s") ? " are low durability! " : " is low durability! ") + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + (int) dura + ChatFormatting.GRAY + "]";
                strings.add(string);
                mc.fontRenderer.drawStringWithShadow(string, x - mc.fontRenderer.getStringWidth(string) / 2f, y + yOffset, Color.RED.getRGB());
                yOffset += mc.fontRenderer.FONT_HEIGHT + 2;
            }
        }
        this.width = MessageUtil.getLongestWordInt(strings);
        this.height = yOffset == 0 ? mc.fontRenderer.FONT_HEIGHT : yOffset;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
