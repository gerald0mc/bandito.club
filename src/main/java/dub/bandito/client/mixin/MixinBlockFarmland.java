package club.bandito.client.mixin;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.movement.AntiPlantStomp;
import club.bandito.client.util.MessageUtil;
import net.minecraft.block.BlockFarmland;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFarmland.class)
public class MixinBlockFarmland {
    @Inject(method = "onFallenUpon", at = @At("HEAD"), cancellable = true)
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance, CallbackInfo ci) {
        AntiPlantStomp module = Bandito.getModuleManager().getModule(AntiPlantStomp.class);
        if (module.isEnabled() && entityIn.equals(Minecraft.getMinecraft().player)) {
            MessageUtil.sendMessage("Stopped you from stomping on plants.", false);
            ci.cancel();
        }
    }
}
