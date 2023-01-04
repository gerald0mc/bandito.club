package club.bandito.client.module.mods.misc;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.module.setting.ModeSetting;
import club.bandito.client.util.InventoryUtil;
import club.bandito.client.util.MessageUtil;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ItemSaver extends Module {
    public ItemSaver() {
        super("ItemSaver", "Attempts to save your item from breaking.", Category.MISC);
    }

    private final ModeSetting mode = new ModeSetting("Mode", "Drop", "Drop", "MoveSlot");
    private final FloatSetting swapDurability = new FloatSetting("SwapDurability", 5, 1, 20);
    private final BooleanSetting message = new BooleanSetting("Message", false);

    @Override
    public String getMetaData() {
        return mode.getMode();
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        ItemStack stack = mc.player.getHeldItemMainhand();
        float durability = ((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage();
        if (stack.getItem() == Items.AIR || Float.isNaN(durability) || Float.isInfinite(durability)) return;
        if (durability <= swapDurability.getValue()) {
            switch (mode.getMode()) {
                case "Drop":
                    mc.player.dropItem(false);
                    if (message.getValue())
                        MessageUtil.sendMessage("Dropped item to avoid breaking tool.", false);
                    return;
                case "MoveSlot":
                    int slot = mc.player.inventory.currentItem;
                    if (slot == 0) {
                        InventoryUtil.switchToSlot(8);
                    } else {
                        InventoryUtil.switchToSlot(slot - 1);
                    }
                    if (message.getValue())
                        MessageUtil.sendMessage("Moved to new slot to avoid breaking tool.", false);
            }
        }
    }
}
