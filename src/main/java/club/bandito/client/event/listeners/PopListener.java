package club.bandito.client.event.listeners;

import club.bandito.client.event.PacketEvent;
import club.bandito.client.event.PopEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class PopListener {
    public PopListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    //Create a new instance of a HashMap for the pop count map
    public HashMap<Entity, Integer> popMap = new HashMap<>();

    @SubscribeEvent
    public void onPacketR(PacketEvent.Receive event) {
        //Checks if the received packet is a instanceof SPacketEntityStatus
        if (event.getPacket() instanceof SPacketEntityStatus) {
            //Casts the getpacket to SPacketEntityStatus
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            //Checks the packets opCode to make sure it is the correct code for a totem pop
            if (packet.getOpCode() == 35) {
                //Checks if the popMap does or does not contain the entity being effected
                if (!popMap.containsKey(packet.getEntity(Minecraft.getMinecraft().world))) {
                    //Posts a new PopEvent for module use
                    MinecraftForge.EVENT_BUS.post(new PopEvent(packet.getEntity(Minecraft.getMinecraft().world), 1));
                    //Puts the event to the popMap
                    popMap.put(packet.getEntity(Minecraft.getMinecraft().world), 1);
                } else {
                    //Puts the entities popCount to one higher in the hashMap
                    popMap.put(packet.getEntity(Minecraft.getMinecraft().world), popMap.get(packet.getEntity(Minecraft.getMinecraft().world)) + 1);
                    //Posts a new PopEvent for module use
                    MinecraftForge.EVENT_BUS.post(new PopEvent(packet.getEntity(Minecraft.getMinecraft().world), popMap.get(packet.getEntity(Minecraft.getMinecraft().world))));
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        //Checks if the world or the player are null
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            //Loops through all the loaded playerEntities in the world at that moment
            for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                //Checks to see if the player is dead or not
                if ((player.isDead || !player.isEntityAlive() || player.getHealth() <= 0)) {
                    //Remove the player from the popMap if so
                    popMap.remove(player);
                }
            }
        }
    }
}
