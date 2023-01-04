package club.bandito.client.mixin;

import net.minecraft.block.BlockFarmland;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockFarmland.class)
public interface IBlockFarmland {
    @Invoker("hasCrops")
    boolean hasCropsAccessor(World worldIn, BlockPos pos);
}
