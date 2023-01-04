package club.bandito.client.module.mods.hud.mods.totemc;

import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.RenderUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TotemCountComponent extends HUDComponent {
    public TotemCountComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        ItemStack stack = new ItemStack(Items.TOTEM_OF_UNDYING);
        RenderUtil.renderItem(stack, String.valueOf(RenderUtil.getTotalAmountOfItem(Items.TOTEM_OF_UNDYING)), x, y);
        this.width = 17;
        this.height = 17;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
