package club.bandito.client.mixin;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.render.NoRender;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Inject(method = "handleEntityStatus", at = @At("HEAD"), cancellable = true)
    void handleEntityStatus(SPacketEntityStatus packetIn, CallbackInfo ci) {
        NoRender module = Bandito.getModuleManager().getModule(NoRender.class);
        if (packetIn.getOpCode() == 35) {
            if (module.isEnabled() && module.totemPops.getValue()) {
                ci.cancel();
            }
        }
    }
}
