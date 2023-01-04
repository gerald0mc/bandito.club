package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.Module;
import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.util.MessageUtil;

public class Reset extends Command {
    public Reset() {
        super("Reset", "Resets a HUD modules X Y position if it is stuck off the screen.", new String[] {"reset", "[modName]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //Checks if the amount of args entered is the correct amount or not
        if (args.length != 2) {
            MessageUtil.sendMessage("Please perform the help command to see how to perform this command.", false);
            return;
        }
        //Gets this value from the second arg
        String modName = args[1];
        //Loop through all of the modules in the client
        for (Module module : Bandito.getModuleManager().getModules()) {
            //Check if the currently looped through module is equalIgnoreCase of the modName
            if (module.getName().equalsIgnoreCase(modName)) {
                //Checks if the module you entered is a HUD module or not
                if (!(module instanceof HUDModule)) {
                    MessageUtil.sendMessage("The module you entered to reset is not a HUD module.", false);
                    return;
                }
                //Cast the module to its HUDModule counterpart
                final HUDModule hudModule = (HUDModule) module;
                //Sets the hudModules x to 0
                hudModule.getComponent().x = 0;
                //Sets the hudModules y to 0
                hudModule.getComponent().y = 0;
                //Sends a client side message
                MessageUtil.sendMessage("Reset " + module.getName() + " X Y coordinates.", false);
                return;
            }
        }
        //Catch for if the command failed
        MessageUtil.sendMessage("Couldn't find the module you entered.", false);
    }
}
