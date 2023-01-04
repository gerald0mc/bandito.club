package club.bandito.client.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface IMinecraft {

    @Invoker("rightClickMouse")
    void bandito_rightClick();

    @Invoker("clickMouse")
    void bandito_leftClick();

    @Invoker("middleClickMouse")
    void bandito_middleClick();
}
