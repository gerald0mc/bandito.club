package club.bandito.client.module.mods.misc;

import baritone.api.BaritoneAPI;
import baritone.api.utils.BetterBlockPos;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.ListSetting;
import club.bandito.client.module.setting.PositionSetting;
import club.bandito.client.module.setting.Setting;
import club.bandito.client.util.InventoryUtil;
import club.bandito.client.util.MessageUtil;
import club.bandito.client.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gav06 & gerald0mc
 * @date 6/23/22
 */

public class ObbyBot extends Module {

    public ListSetting cornerList = new ListSetting("Corners", new Setting[] {
        new PositionSetting("Corner1"),
        new PositionSetting("Corner2")
    });
    public ListSetting portalList = new ListSetting("Portals", new Setting[] {
            new PositionSetting("OWPortal"),
            new PositionSetting("NPortal")
    });

    private EnumStage currentStage = null;
    private boolean ranStage = false;
    private Timer totalTime = null;
    private int cycle = 1;

    public ObbyBot() {
        super("ObbyBot", "bot to mine obsidian", Category.MISC);
    }

    @Override
    public String getMetaData() {
        return currentStage != null ? currentStage.name() : "";
    }

    @Override
    protected void onEnable() {
        if (nullCheck()) {
            toggle();
            return;
        }
        PositionSetting cornerOne = (PositionSetting) cornerList.getList().get(0);
        PositionSetting cornerTwo = (PositionSetting) cornerList.getList().get(1);
        PositionSetting overworldPortal = (PositionSetting) portalList.getList().get(0);
        PositionSetting netherPortal = (PositionSetting) portalList.getList().get(1);
        boolean fail = false;
        if (cornerOne.getPosition() == null) {
            MessageUtil.sendMessage("No Corner1 position set", false);
            fail = true;
        }
        if (cornerTwo.getPosition() == null) {
            MessageUtil.sendMessage("No Corner2 position set", false);
            fail = true;
        }
        if (overworldPortal.getPosition() == null) {
            MessageUtil.sendMessage("No Overworld Portal position set", false);
            fail = true;
        }
        if (netherPortal.getPosition() == null) {
            MessageUtil.sendMessage("No Nether Portal position set", false);
            fail = true;
        }

        if (fail) {
            disable();
        } else {
            currentStage = EnumStage.SETUP;
            if (totalTime == null)
                totalTime = new Timer();
        }
    }

    @Override
    protected void onDisable() {
        if (nullCheck()) return;
        mc.player.sendChatMessage("#stop");
        mc.player.sendChatMessage("#sel clear");
        if (totalTime != null) {
            int seconds = (int) ((System.currentTimeMillis() - totalTime.getLastMs()) / 1000) % 60 ;
            int minutes = (int) (((System.currentTimeMillis() - totalTime.getLastMs()) / (1000*60)) % 60);
            int hours   = (int) (((System.currentTimeMillis() - totalTime.getLastMs()) / (1000*60*60)) % 24);
            MessageUtil.sendMessage("Total Time: " + hours + " hours " + minutes + " mins " + seconds + " secs Total Cycles: " + cycle + " Total Mined Obsidian: " + (cycle * 10), false);
            totalTime = null;
        }
        cycle = 1;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        if (totalTime == null) return;
        int seconds = (int) ((System.currentTimeMillis() - totalTime.getLastMs()) / 1000) % 60 ;
        int minutes = (int) (((System.currentTimeMillis() - totalTime.getLastMs()) / (1000*60)) % 60);
        int hours   = (int) (((System.currentTimeMillis() - totalTime.getLastMs()) / (1000*60*60)) % 24);
        LinkedList<String> hudStrings = new LinkedList<>();
        hudStrings.add("ObbyBot");
        hudStrings.add("Made by: Gav06 and gerald0mc");
        hudStrings.add("Stage: " + currentStage.name());
        hudStrings.add("Cycle: " + cycle);
        hudStrings.add("Total Time: " + hours + " hours " + minutes + " mins " + seconds + " secs");
        Gui.drawRect(0, 0, MessageUtil.getLongestWordInt(hudStrings) + 4, hudStrings.size() * (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) + 2, new Color(80, 80, 80, 255).getRGB());
        int yOffset = 0;
        for (String s : hudStrings) {
            mc.fontRenderer.drawStringWithShadow(s, 2, 1 + yOffset, -1);
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            toggle();
            return;
        }
        PositionSetting cornerOne = (PositionSetting) cornerList.getList().get(0);
        PositionSetting cornerTwo = (PositionSetting) cornerList.getList().get(1);
        PositionSetting overworldPortal = (PositionSetting) portalList.getList().get(0);
        PositionSetting netherPortal = (PositionSetting) portalList.getList().get(1);
        switch (currentStage) {
            case SETUP:
                BaritoneAPI.getProvider().getPrimaryBaritone().getSelectionManager()
                        .addSelection(new BetterBlockPos(cornerOne.getPosition()), new BetterBlockPos(cornerTwo.getPosition().add(0, 5, 0)));
                if (!ranStage) {
                    mc.player.sendChatMessage("#sel cleararea");
                    ranStage = true;
                } else if (!BaritoneAPI.getProvider().getPrimaryBaritone().getBuilderProcess().isActive()) {
                    currentStage = EnumStage.OVERWORLD_APPROACH;
                    ranStage = false;
                }
                break;
            case OVERWORLD_APPROACH:
                if (mc.player.dimension == 0 && !ranStage) {
                    final BlockPos portal = overworldPortal.getPosition();
                    mc.player.sendChatMessage("#goto " + portal.getX() + " " + portal.getY() + " " + portal.getZ());
                    ranStage = true;
                } else if (mc.player.dimension == -1) {
                    mc.player.sendChatMessage("#stop");
                    ranStage = false;
                    currentStage = EnumStage.NETHER_APPROACH;
                }
                break;
            case NETHER_APPROACH:
                if (mc.player.dimension == -1 && !ranStage) {
                    final BlockPos portal = netherPortal.getPosition();
                    mc.player.sendChatMessage("#goto " + portal.getX() + " " + portal.getY() + " " + portal.getZ());
                    ranStage = true;
                }

                if (mc.player.dimension != -1) {
                    ranStage = false;
                    mc.player.sendChatMessage("#stop");
                    currentStage = EnumStage.OBSIDIAN_MINE;
                }
                break;
            case OBSIDIAN_MINE:
                float dura = ((mc.player.getHeldItemMainhand().getMaxDamage() - mc.player.getHeldItemMainhand().getItemDamage()) * 100f) / (float) mc.player.getHeldItemMainhand().getMaxDamage();
                if (dura < 5 && mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_PICKAXE)) {
                    int picSlot = InventoryUtil.getItemInventory(Items.DIAMOND_PICKAXE, true, true, 5, true);
                    if (picSlot == -1) {
                        mc.player.connection.sendPacket(new SPacketDisconnect(new TextComponentString("No pickaxes in inventory logged and toggled.")));
                        toggle();
                        return;
                    }
                    int airSlot = InventoryUtil.getItemHotbar(Items.AIR);
                    if (airSlot != -1 && airSlot != mc.player.inventory.currentItem) {
                        InventoryUtil.moveItemToSlot(airSlot, picSlot);
                        return;
                    }
                    InventoryUtil.moveItemToSlot(9, picSlot);
                }
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.PORTAL)) {
                    mc.player.sendChatMessage("#goto " + (mc.player.posX + 1) + " " + mc.player.posY + " " + (mc.player.posZ + 1));
                    return;
                }
                if (!ranStage) {
                    mc.player.sendChatMessage("#stop");
                    mc.player.sendChatMessage("#sel cleararea");
                    ranStage = true;
                } else {
                    if (!BaritoneAPI.getProvider().getPrimaryBaritone().getBuilderProcess().isActive()) {
                        ranStage = false;
                        currentStage = EnumStage.OVERWORLD_APPROACH;
                        cycle++;
                    }
                }
                break;
        }
    }

    private enum EnumStage {
        SETUP,
        OVERWORLD_APPROACH,
        NETHER_APPROACH,
        OBSIDIAN_MINE;
    }
}
