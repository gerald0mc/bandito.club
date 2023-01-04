package club.bandito.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    public static int getItemHotbar(Item item) {
        for (int i = 0; i < 9; i++) {
            if (Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar) {
        for (int i = (hotbar ? 0 : 9); i < 45; ++i) {
            ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
            if (stack.getItem() == item)
                return i;
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar, boolean skipHand) {
        for (int i = (hotbar ? 0 : 9); i < 45; ++i) {
            if (Minecraft.getMinecraft().player.inventory.currentItem == i && skipHand) continue;
            ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
            float stackDura = ((Minecraft.getMinecraft().player.getHeldItemMainhand().getMaxDamage() - Minecraft.getMinecraft().player.getHeldItemMainhand().getItemDamage()) * 100f) / (float) Minecraft.getMinecraft().player.getHeldItemMainhand().getMaxDamage();
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar, boolean countSkip, int count) {
        for (int i = (hotbar ? 0 : 9); i < 45; ++i) {
            ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
            if (countSkip && stack.getCount() < count) continue;
            if (stack.getItem() == item)
                return i;
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar, boolean duraSkip, float dura) {
        for (int i = (hotbar ? 0 : 9); i < 45; ++i) {
            ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
            float stackDura = ((Minecraft.getMinecraft().player.getHeldItemMainhand().getMaxDamage() - Minecraft.getMinecraft().player.getHeldItemMainhand().getItemDamage()) * 100f) / (float) Minecraft.getMinecraft().player.getHeldItemMainhand().getMaxDamage();
            if (duraSkip && !Float.isNaN(stackDura) && (stackDura <= dura)) continue;
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar, boolean duraSkip, float dura, boolean skipHand) {
        for (int i = (hotbar ? 0 : 9); i < 45; ++i) {
            if (Minecraft.getMinecraft().player.inventory.currentItem == i && skipHand) continue;
            ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
            float stackDura = ((Minecraft.getMinecraft().player.getHeldItemMainhand().getMaxDamage() - Minecraft.getMinecraft().player.getHeldItemMainhand().getItemDamage()) * 100f) / (float) Minecraft.getMinecraft().player.getHeldItemMainhand().getMaxDamage();
            if (duraSkip && !Float.isNaN(stackDura) && (stackDura <= dura)) continue;
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getTotalItem(Item item) {
        int amountOfItem = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack.getItem() == item)
                amountOfItem += stack.getCount();
        }
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item)
            amountOfItem += Minecraft.getMinecraft().player.getHeldItemOffhand().getCount();
        return amountOfItem;
    }

    public static int getTotalStack(Item item) {
        int amountOfStacks = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack.getItem() == item)
                amountOfStacks += 1;
        }
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item)
            amountOfStacks += 1;
        return amountOfStacks;
    }

    public static boolean isInventoryFull(boolean hotbar) {
        for (int i = (hotbar ? 0 : 9); i < 45; ++i) {
            ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
            if (stack.getItem().equals(Items.AIR)) return false;
        }
        return true;
    }

    public static void moveItemToSlot(int startSlot, int endSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
    }

    public static void silentSwitchToSlot(int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static void switchToSlot(int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().player.inventory.currentItem = slot;
        Minecraft.getMinecraft().playerController.updateController();
    }
}
