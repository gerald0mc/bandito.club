package club.bandito.client.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PopEvent extends Event {
    private final Entity player;
    private final int popCount;

    public PopEvent(Entity player, int popCount) {
        this.player = player;
        this.popCount = popCount;
    }

    public Entity getPlayer() {
        return player;
    }

    public int getPopCount() {
        return popCount;
    }
}
