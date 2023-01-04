package club.bandito.client.mixin;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.world.AFKMine;
import club.bandito.client.module.mods.movement.NoMovement;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions {
    @Inject(method = "updatePlayerMoveState", at = @At("HEAD"), cancellable = true)
    void handleUpdatePlayerMoveState(CallbackInfo ci) {
        NoMovement noMovement = Bandito.getModuleManager().getModule(NoMovement.class);
        AFKMine afkMine = Bandito.getModuleManager().getModule(AFKMine.class);
        if (noMovement.isEnabled() || afkMine.isEnabled() && afkMine.cancelMovement.getValue()) {
            ci.cancel();
        }
    }
}
