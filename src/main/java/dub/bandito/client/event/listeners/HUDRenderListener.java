package club.bandito.client.event.listeners;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.module.mods.hud.HUDModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDRenderListener {
    public HUDRenderListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        //Loop through all the modules in the client
        for (Module module : Bandito.getModuleManager().getModules()) {
            //Checks if the modules category is equal to HUD
            if (module.getCategory() == Module.Category.HUD) {
                //Checks if the module is enabled
                if (module.isEnabled()) {
                    //Casts the module to its respective HUDModule state
                    HUDModule hudModule = (HUDModule) module;
                    //Grabs the HUDComponent from the HUDModule we just created
                    HUDComponent component = hudModule.getComponent();
                    //Checks if the curent screen is equal to the ClickGUI
                    if (Minecraft.getMinecraft().currentScreen == Bandito.getClickGui())
                        //Renders a square behind the component if so
                        Gui.drawRect(component.x - 1, component.y - 1, component.x + component.width, component.y + component.height, component.isInside() ? 0x50ffffff : 0x90000000);
                    //Performs the components doRender abstract method
                    hudModule.getComponent().doRender();
                }
            }
        }
    }
}
