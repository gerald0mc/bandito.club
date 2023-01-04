package club.bandito.client.module.mods.world;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.Timer;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Stealer extends Module {
    public Stealer() {
        super("Stealer", "Steals items from different containers.", Category.WORLD);
    }

    private final FloatSetting delay = new FloatSetting("Delay", 50, 0, 250);

    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        if (mc.currentScreen instanceof GuiShulkerBox) {
            GuiShulkerBox gui = (GuiShulkerBox) mc.currentScreen;
            for (int i = 0; i < 27; i++) {
                if (!timer.passed((long) delay.getValue())) return;
                ItemStack stack = mc.player.openContainer.getSlot(i).getStack();
                if (stack.getItem() == Items.AIR) continue;
                int slot = -1;
                for (int i1 = 44; i1 > 9; i1--) {
                    ItemStack s = mc.player.inventoryContainer.getSlot(i1).getStack();
                    if (s.getItem() == Items.AIR || s.getItem() == stack.getItem() && s.getCount() < s.getMaxStackSize() && s.getDisplayName().equals(stack.getDisplayName()))
                        slot = i1;
                }
                if (slot == -1) return;
                mc.playerController.windowClick(gui.inventorySlots.windowId, i, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(gui.inventorySlots.windowId, 18 + slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(gui.inventorySlots.windowId, i, 0, ClickType.PICKUP, mc.player);
                timer.reset();
            }
        }
    }
}
