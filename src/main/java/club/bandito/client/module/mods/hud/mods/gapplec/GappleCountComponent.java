package club.bandito.client.module.mods.hud.mods.gapplec;

import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.RenderUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GappleCountComponent extends HUDComponent {
    public GappleCountComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        ItemStack stack = new ItemStack(Items.GOLDEN_APPLE);
        RenderUtil.renderItem(stack, String.valueOf(RenderUtil.getTotalAmountOfItem(Items.GOLDEN_APPLE)), x, y);
        this.width = 17;
        this.height = 17;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
