package club.bandito.client.module.mods.hud.mods.crystalc;

import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.RenderUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CrystalCountComponent extends HUDComponent {
    public CrystalCountComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        ItemStack stack = new ItemStack(Items.END_CRYSTAL);
        RenderUtil.renderItem(stack, String.valueOf(RenderUtil.getTotalAmountOfItem(Items.END_CRYSTAL)), x, y);
        this.width = 17;
        this.height = 17;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
