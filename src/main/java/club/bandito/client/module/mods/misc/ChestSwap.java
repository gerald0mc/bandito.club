package club.bandito.client.module.mods.misc;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.util.InventoryUtil;
import club.bandito.client.util.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class ChestSwap extends Module {
    public ChestSwap() {
        super("ChestSwap", "On enable atempts to switch to another armor piece.", Category.MISC);
    }

    private final BooleanSetting debugMessages = new BooleanSetting("DebugMessages", false);

    private final Item[] chestPieces = new Item[] {Items.DIAMOND_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.IRON_CHESTPLATE};

    @Override
    public void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        Slot chestSlot = mc.player.inventoryContainer.getSlot(6);
        boolean debug = debugMessages.getValue();
        if (chestSlot.getStack().getItem() == Items.ELYTRA) {
            for (Item i : chestPieces) {
                int cSlot = InventoryUtil.getItemInventory(i, false);
                if (cSlot == -1) continue;
                InventoryUtil.moveItemToSlot(6, cSlot);
                if (debug)
                    MessageUtil.sendMessage("Switched to ChestPlate.", false);
                toggle();
                return;
            }
            MessageUtil.sendMessage("No ChestPlate to switch to.", false);
        } else if (chestSlot.getStack().getItem() instanceof ItemArmor) {
            int eSlot = InventoryUtil.getItemInventory(Items.ELYTRA, false);
            if (eSlot == -1) {
                MessageUtil.sendMessage("No Elytra to switch to.", false);
                toggle();
                return;
            }
            InventoryUtil.moveItemToSlot(6, eSlot);
            if (debug)
                MessageUtil.sendMessage("Switched to Elytra.", false);
        } else if (chestSlot.getStack().getItem() == Items.AIR) {
            for (Item i : chestPieces) {
                int cSlot = InventoryUtil.getItemInventory(i, false);
                if (cSlot == -1) continue;
                InventoryUtil.moveItemToSlot(6, cSlot);
                if (debug)
                    MessageUtil.sendMessage("Switched to ChestPlate.", false);
                toggle();
                return;
            }
            MessageUtil.sendMessage("No ChestPlate to switch to.", false);
        }
        toggle();
    }
}
