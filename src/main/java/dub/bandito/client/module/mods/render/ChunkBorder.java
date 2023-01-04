package club.bandito.client.module.mods.render;

import club.bandito.client.module.Module;
import club.bandito.client.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChunkBorder extends Module {
    public ChunkBorder() {
        super("ChunkBorder", "Renders a box around chunks.", Category.RENDER);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        BlockPos from = new BlockPos(mc.player.chunkCoordX * 16, 0, mc.player.chunkCoordZ * 16);
        BlockPos to = new BlockPos(from.getX() + 16, 256, from.getZ() + 16);
        AxisAlignedBB box = new AxisAlignedBB(from, to).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        RenderUtil.prepare();
        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1f, 0f, 0f, 1f);
        RenderUtil.release();
    }
}
