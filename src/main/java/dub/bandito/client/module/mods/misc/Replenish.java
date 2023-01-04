package club.bandito.client.module.mods.misc;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Replenish extends Module {
    public Replenish() {
        super("Replenish", "Puts more of different items into slots.", Category.MISC);
    }

    public FloatSetting amount = new FloatSetting("Amount", 10, 1, 15);

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getMaxStackSize() == 1 || stack.getItem().equals(Items.AIR)) continue;
            if (stack.getCount() <= (int) amount.getValue()) {
                int itemSlot = InventoryUtil.getItemInventory(stack.getItem(), false);
                if (itemSlot != -1) {
                    ItemStack replenishStack = mc.player.inventory.getStackInSlot(itemSlot);
                    if (!stack.getDisplayName().equals(replenishStack.getDisplayName())) continue;
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, itemSlot, 0, ClickType.QUICK_MOVE, mc.player);
                    return;
                }
            }
        }
    }
}
