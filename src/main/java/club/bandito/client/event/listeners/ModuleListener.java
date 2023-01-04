package club.bandito.client.event.listeners;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleListener {
    public ModuleListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        //Loop through all the modules in the client
        for (Module module : Bandito.getModuleManager().getModules()) {
            //Checks if the module is not enabled and is set to alwaysEnabled
            if (!module.isEnabled() && module.isAlwaysEnabled()) {
                //Toggles the module
                module.enable();
            }
        }
    }
}
