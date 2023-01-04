package club.bandito.client.module.mods.misc;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.InventoryUtil;
import club.bandito.client.util.MessageUtil;
import club.bandito.client.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoShear extends Module {
    public AutoShear() {
        super("AutoShear", "Automatically shears sheep for you.", Category.MISC);
    }

    private final FloatSetting delay = new FloatSetting("Delay", 500, 1, 1000);
    private final FloatSetting range = new FloatSetting("Range", 4, 1, 6);

    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        if (!timer.passed((long) delay.getValue())) return;
        for (Entity e : mc.world.loadedEntityList) {
            if (e.getDistance(mc.player) >= range.getValue()) continue;
            if (e instanceof EntitySheep) {
                EntitySheep sheep = (EntitySheep) e;
                if (!sheep.getSheared()) {
                    int originalSlot = -1;
                    if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemShears)) {
                        int shearSlot = InventoryUtil.getItemHotbar(Items.SHEARS);
                        originalSlot = mc.player.inventory.currentItem;
                        if (shearSlot == -1) {
                            MessageUtil.sendMessage("You have no shears to switch to now toggling!", false);
                            toggle();
                            return;
                        }
                        InventoryUtil.switchToSlot(shearSlot);
                    }
                    mc.playerController.interactWithEntity(mc.player, e, EnumHand.MAIN_HAND);
                    if (originalSlot != -1 && originalSlot != mc.player.inventory.currentItem) {
                        InventoryUtil.switchToSlot(originalSlot);
                    }
                    timer.reset();
                    return;
                }
            }
        }
    }
}
