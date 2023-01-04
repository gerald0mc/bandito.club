package club.bandito.client.module.mods.combat;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.mixin.IEntity;
import club.bandito.client.module.Module;
import club.bandito.client.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiWeb extends Module {
    public AntiWeb() {
        super("AntiWeb", "Destroys webs since people are fags.", Category.COMBAT);
    }

    private final Item[] swords = {Items.DIAMOND_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.STONE_SWORD, Items.WOODEN_SWORD};
    private int originalSlot = -1;

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        if (((IEntity) mc.player).getIsInWeb()) {
            if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                for (Item item : swords) {
                    int swordSlot = InventoryUtil.getItemHotbar(item);
                    if (swordSlot != -1 && mc.player.inventory.currentItem != swordSlot) {
                        originalSlot = mc.player.inventory.currentItem;
                        InventoryUtil.switchToSlot(swordSlot);
                        break;
                    }
                }
            }
            BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, playerPos, EnumFacing.getDirectionFromEntityLiving(playerPos, mc.player)));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, playerPos, EnumFacing.getDirectionFromEntityLiving(playerPos, mc.player)));
        } else if (originalSlot != -1) {
            if (originalSlot == mc.player.inventory.currentItem) {
                originalSlot = -1;
                return;
            }
            InventoryUtil.switchToSlot(originalSlot);
            originalSlot = -1;
        }
    }
}
