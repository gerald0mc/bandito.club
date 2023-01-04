package club.bandito.client.module.mods.world;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.BooleanSetting;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.InventoryUtil;
import club.bandito.client.util.MessageUtil;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class CowDupe extends Module {
    private final FloatSetting factor = new FloatSetting("Factor", 150, 10, 999);
    private final BooleanSetting shearSwitch = new BooleanSetting("ShearSwitch", true);

    public CowDupe() {
        super("CowDupe", "Be looking at a mooshroom when you enable this module.", Category.WORLD);
    }

    @Override
    protected void onEnable() {
        if (mc.pointedEntity == null) {
            MessageUtil.sendMessage("Please be pointing at a entity to perform this module!", false);
            toggle();
            return;
        }
        if (!mc.player.getHeldItemMainhand().getItem().equals(Items.SHEARS)) {
            if (shearSwitch.getValue()) {
                int shearSlot = InventoryUtil.getItemHotbar(Items.SHEARS);
                if (shearSlot != -1) {
                    InventoryUtil.switchToSlot(shearSlot);
                } else {
                    MessageUtil.sendMessage("Please either have shears in your Hotbar or your Main-hand to perform this module.", false);
                    toggle();
                    return;
                }
            } else {
                MessageUtil.sendMessage("You need to hold shears to perform this module.", false);
                toggle();
                return;
            }
        }
        for (int i = 0; i < (int) factor.getValue(); ++i) {
            if (!(mc.pointedEntity instanceof EntityMooshroom)) {
                MessageUtil.sendMessage("Finished shearing targeted entity.", false);
                toggle();
                return;
            }
            mc.player.connection.sendPacket(new CPacketUseEntity(mc.pointedEntity, EnumHand.MAIN_HAND));
        }
        MessageUtil.sendMessage("Finished shearing targeted entity.", false);
        toggle();
    }
}
