package club.bandito.client.module.mods.world;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.MessageUtil;
import club.bandito.client.util.RenderUtil;
import club.bandito.client.util.Timer;
import club.bandito.client.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Nuker extends Module {
    public FloatSetting delay = new FloatSetting("Delay(MS)", 250, 0, 5000);
    public FloatSetting range = new FloatSetting("Range", 4, 1, 6);
    public FloatSetting boxAlpha = new FloatSetting("BoxAlpha", 125, 0, 255);

    public Queue<BlockPos> breakQueue = new ConcurrentLinkedDeque<>();
    public Block targetBlock = null;
    public Timer timer = new Timer();

    public Nuker() {
        super("Nuker", "Nuker but with packets.", Category.WORLD);
    }

    @Override
    public String getMetaData() {
        if (targetBlock == null) return null;
        return targetBlock.getLocalizedName();
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            if (!mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.BLOCK)) return;
            int button = Mouse.getEventButton();
            if (button == 0 && targetBlock == null) {
                targetBlock = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
                MessageUtil.sendMessage("Set target block to " + targetBlock.getLocalizedName() + ".", false);
                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        if (targetBlock == null) return;
        if (!breakQueue.isEmpty()) {
            if (mc.world.getBlockState(breakQueue.peek()).getBlock().equals(Blocks.AIR) || mc.player.getDistance(breakQueue.peek().getX(), breakQueue.peek().getY(), breakQueue.peek().getZ()) >= range.getValue()) {
                breakQueue.remove();
                return;
            }
            if (!timer.passed((long) delay.getValue())) return;
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, breakQueue.peek(), EnumFacing.getDirectionFromEntityLiving(breakQueue.peek(), mc.player)));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakQueue.peek(), EnumFacing.getDirectionFromEntityLiving(breakQueue.peek(), mc.player)));
            timer.reset();
        }
        for (BlockPos pos : WorldUtil.getSphere(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), (int) range.getValue())) {
            Block block = mc.world.getBlockState(pos).getBlock();
            if (breakQueue.contains(pos)) continue;
            if (block.equals(targetBlock)) {
                breakQueue.add(pos);
                if (timer.passed((long) delay.getValue())) timer.reset();
                return;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        if (targetBlock == null) return;
        if (breakQueue.isEmpty()) return;
        for (BlockPos pos : breakQueue) {
            AxisAlignedBB box = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1f, 0f, 0f, boxAlpha.getValue() / 255f);
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1f, 0f, 0f, 1f);
            RenderUtil.release();
        }
    }
}
