package club.bandito.client.module.mods.misc;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.util.InventoryUtil;
import club.bandito.client.util.MessageUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.asm.transformers.PotionEffectTransformer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class AFKMend extends Module {
    public BooleanSetting skipArmor = new BooleanSetting("SkipArmor", true);

    public AFKMend() {
        super("AFKMend", "Puts items that need mending in your offhand.", Category.MISC);
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        float offhandDura = ((mc.player.getHeldItemOffhand().getMaxDamage() - mc.player.getHeldItemOffhand().getItemDamage()) * 100f) / (float) mc.player.getHeldItemOffhand().getMaxDamage();
        if (mc.player.getHeldItemOffhand().getItem().equals(Items.AIR) || offhandDura == 100 || Float.isNaN(offhandDura) || Float.isInfinite(offhandDura))
            switchToNewItem();
    }

    public void switchToNewItem() {
        for (int i = 0; i < 45; i++) {
            if (i == mc.player.inventory.currentItem) continue;
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!hasMending(stack) || stack.getItem().equals(Items.AIR)) continue;
            if (skipArmor.getValue() && stack.getItem() instanceof ItemArmor) continue;
            float dura = ((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage();
            if (dura < 100) {
                InventoryUtil.moveItemToSlot(45, i);
                MessageUtil.sendMessage("Moved new item to offhand.", false);
                return;
            }
        }
        int airSlot = InventoryUtil.getItemInventory(Items.AIR, false);
        if (airSlot != -1)
            InventoryUtil.moveItemToSlot(45, airSlot);
        MessageUtil.sendMessage("No more items left to mend toggling module.", false);
        toggle();
    }

    public boolean hasMending(ItemStack stack) {
        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet())
            if (entry.getKey().equals(Enchantments.MENDING)) return true;
        return false;
    }
}
