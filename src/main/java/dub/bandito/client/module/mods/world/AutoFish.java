package club.bandito.client.module.mods.world;

import club.bandito.client.event.PacketEvent;
import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.mixin.IMinecraft;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.Timer;
import net.minecraft.block.BlockLadder;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoFish extends Module {

    private final FloatSetting castTime = new FloatSetting("CastTime", 2f, 0.1f, 10f);

    private boolean shouldCast = false;
    private boolean shouldReel = false;

    private final Timer castTimer = new Timer();

    public AutoFish() {
        super("AutoFish", "Fishes for you after first cast.", Category.WORLD);
    }

    @Override
    public void onDisable() {
        if (shouldCast)
            shouldCast = false;
        if (shouldReel) {
            rightClick();
            shouldReel = false;
        }
    }

    @SubscribeEvent
    public void onTick(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        final long ms = ((long) castTime.getValue()) * 1000L;
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
            if (shouldReel) {
                rightClick();
                shouldReel = false;
                shouldCast = true;
                castTimer.reset();
            } else if (shouldCast && castTimer.passed(ms)) {
                rightClick();
                shouldCast = false;
            }
        }
    }

    @SubscribeEvent
    public void onPacketRead(PacketEvent.Receive event) {
        if (mc.player == null || mc.player.fishEntity == null)
            return;
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect soundPacket = (SPacketSoundEffect) event.getPacket();

            if (soundPacket.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
                final EntityFishHook hook = mc.player.fishEntity;
                if (Math.abs(soundPacket.getX() - hook.posX) <= 1.0
                        && Math.abs(soundPacket.getY() - hook.posY) <= 1.0
                        && Math.abs(soundPacket.getZ() - hook.posZ) <= 1.0 ) {
                    shouldReel = true;
                }
            }
        }
    }

    private void rightClick() {
        ((IMinecraft)mc).bandito_rightClick();
    }
}
