package club.bandito.client.mixin;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.render.NoRender;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "spawnParticle(IZDDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    void spawnParticleHook(int particleID, boolean ignoreRange, double xCood, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters, CallbackInfo ci) {
        NoRender module = Bandito.getModuleManager().getModule(NoRender.class);
        if (module.isEnabled() && module.particles.getValue()) ci.cancel();
    }
}
