package club.bandito.client.mixin;

import net.minecraft.client.gui.GuiMerchant;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(GuiMerchant.class)
public interface IGuiMerchant {
    @Accessor("selectedMerchantRecipe")
    int getSelectedMerchantRecipe();
}
