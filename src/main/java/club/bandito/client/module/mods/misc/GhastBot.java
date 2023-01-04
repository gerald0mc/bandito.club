package club.bandito.client.module.mods.misc;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.PositionSetting;
import club.bandito.client.util.FileUtil;
import club.bandito.client.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author gerald0mc
 */

public class GhastBot extends Module {
    public GhastBot() {
        super("GhastBot", "Farms ghasts for you.", Category.MISC);
    }

    private final PositionSetting afkPos = new PositionSetting("AFKPos");

    private EnumStage currentStage = null;
    private EntityGhast targetGhast = null;
    private boolean ranStage = false;

    @Override
    public String getMetaData() {
        return currentStage != null ? currentStage.name() : "";
    }

    @Override
    public void onEnable() {
        boolean failed = afkPos.getPosition() == null;
        if (nullCheck()) {
            toggle();
            return;
        } else if (!FileUtil.hasBaritoneJar()) {
            MessageUtil.sendMessage("You don't have the baritone jar in your mods folder, please fix that.", false);
            toggle();
            return;
        } else if (failed) {
            MessageUtil.sendMessage("Your AFKPosition is set to null still.", false);
            toggle();
            return;
        }
        BlockPos playerPos = new BlockPos((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ);
        if (!playerPos.equals(afkPos.getPosition())) {
            currentStage = EnumStage.MOVING_TO_AFK;
        } else {
            currentStage = EnumStage.AFK;
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            toggle();
            return;
        }
        switch (currentStage) {
            case MOVING_TO_AFK:
                BlockPos playerPos = new BlockPos((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ);
                if (!ranStage && !playerPos.equals(afkPos.getPosition())) {
                    MessageUtil.sendMessage("Moving to AFKPos.", false);
                    mc.player.sendChatMessage("#goto " + afkPos.getPosition().getX() + " " + afkPos.getPosition().getY() + " " + afkPos.getPosition().getZ());
                    ranStage = true;
                }
                if (playerPos.equals(afkPos.getPosition())) {
                    mc.player.sendChatMessage("#stop");
                    MessageUtil.sendMessage("Moving to AFK stage.", false);
                    currentStage = EnumStage.AFK;
                    ranStage = false;
                    return;
                }
                break;
            case AFK:
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityGhast) {
                        targetGhast = (EntityGhast) e;
                        mc.player.sendChatMessage("#stop");
                        mc.player.sendChatMessage("#goto " + e.posX + " " + e.posY + " " + e.posZ);
                        MessageUtil.sendMessage("Moving to Ghast Pos.", false);
                        currentStage = EnumStage.KILLING_GHAST;
                        return;
                    }
                }
            case KILLING_GHAST:
                for (Entity e : mc.world.loadedEntityList) {
                    if (e.equals(targetGhast)) {
                        if (e.getPosition().getDistance((int) targetGhast.posX, (int) targetGhast.posY, (int) targetGhast.posZ) >= 3) {
                            targetGhast = (EntityGhast) e;
                            mc.player.sendChatMessage("#goto " + e.posX + " " + e.posY + " " + e.posZ);
                            return;
                        }
                    }
                }
                mc.player.sendChatMessage("#stop");
                currentStage = EnumStage.MOVING_TO_AFK;
        }
    }

    public enum EnumStage {
        AFK,
        KILLING_GHAST,
        MOVING_TO_AFK
    }
}
