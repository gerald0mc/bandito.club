package club.bandito.client.mixin;

import club.bandito.client.event.PlayerLivingTickEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void livingUpdateHook(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerLivingTickEvent());
    }
}
