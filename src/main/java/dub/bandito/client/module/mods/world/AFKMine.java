package club.bandito.client.module.mods.world;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.MessageUtil;
import club.bandito.client.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class AFKMine extends Module {
    public AFKMine() {
        super("AFKMine", "Mines the block you click on for you as long as you stay looking at it.", Category.WORLD);
    }

    private final FloatSetting delay = new FloatSetting("Delay", 100, 0, 500);
    private final FloatSetting range = new FloatSetting("Range", 4, 1, 5);
    public final BooleanSetting cancelMovement = new BooleanSetting("CancelMovement", true);

    private Block targetBlock = null;
    private final Timer delayTimer = new Timer();

    @Override
    public void onEnable() {
        if (targetBlock == null) {
            MessageUtil.sendMessage("Please click on the type of block you wish to AFK mine.", false);
        }
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            int button = Mouse.getEventButton();
            if (button == 0 && targetBlock == null) {
                targetBlock = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
                MessageUtil.sendMessage("Set Target Block to " + targetBlock.getLocalizedName() + ".", false);
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        if (targetBlock == null) return;
        if (mc.objectMouseOver.getBlockPos().getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) >= (int) range.getValue()) return;
        if (!delayTimer.passed((long) delay.getValue())) return;
        Block raytraceBlock = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
        if (raytraceBlock.equals(targetBlock)) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.getDirectionFromEntityLiving(mc.objectMouseOver.getBlockPos(), mc.player)));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mc.objectMouseOver.getBlockPos(), EnumFacing.getDirectionFromEntityLiving(mc.objectMouseOver.getBlockPos(), mc.player)));
            delayTimer.reset();
        }
    }
}
