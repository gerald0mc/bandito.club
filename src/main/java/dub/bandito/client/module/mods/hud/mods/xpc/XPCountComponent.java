package club.bandito.client.module.mods.hud.mods.xpc;

import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.RenderUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class XPCountComponent extends HUDComponent {
    public XPCountComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        ItemStack stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        RenderUtil.renderItem(stack, String.valueOf(RenderUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE)), x, y);
        this.width = 17;
        this.height = 17;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
