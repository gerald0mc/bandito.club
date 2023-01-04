package club.bandito.client.module.mods.render;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.module.setting.ListSetting;
import club.bandito.client.module.setting.Setting;
import club.bandito.client.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BurrowESP extends Module {
    public BurrowESP() {
        super("BurrowESP", "Draws a box whenever a player is in a burrow.", Category.RENDER);
    }

    private final FloatSetting range = new FloatSetting("Range", 15, 1, 20);
    private final BooleanSetting self = new BooleanSetting("Self", true);
    private final ListSetting color = new ListSetting("Color", new Setting[] {
        new FloatSetting("Red", 255, 0, 255),
        new FloatSetting("Green", 0, 0, 255),
        new FloatSetting("Blue", 0, 0, 255),
        new FloatSetting("Alpha", 125, 0, 255)
    });

    private final Block[] burrowBlocks = new Block[] {Blocks.OBSIDIAN, Blocks.ENDER_CHEST, Blocks.ANVIL};

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        float red = ((FloatSetting) color.getList().get(0)).getValue() / 255f;
        float green =  ((FloatSetting) color.getList().get(1)).getValue() / 255f;
        float blue =  ((FloatSetting) color.getList().get(2)).getValue() / 255f;
        float alpha =  ((FloatSetting) color.getList().get(3)).getValue() / 255f;
        if (nullCheck()) return;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player.equals(mc.player) && !self.getValue()) continue;
            if (player.getDistance(mc.player) >= (int) range.getValue()) continue;
            BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
            RenderUtil.prepare();
            for (Block block : burrowBlocks) {
                if (mc.world.getBlockState(pos).getBlock() == block) {
                    AxisAlignedBB box = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                    RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
                    RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, 1f);
                }
            }
            RenderUtil.release();
        }
    }
}
