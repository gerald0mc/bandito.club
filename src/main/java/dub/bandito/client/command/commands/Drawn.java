package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.Module;
import club.bandito.client.util.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;

public class Drawn extends Command {
    public Drawn() {
        super("Drawn", "Sets a module to not render in the Array List.", new String[] {"drawn", "[modName]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //arg length check
        if (args.length != 2) {
            MessageUtil.sendMessage("Please perform the help command to see how to perform this command.", false);
            return;
        }
        //Attempt to parse the arg to a string
        String modName = args[1];
        //Loop through all the modules in the client
        for (Module module : Bandito.getModuleManager().getModules()) {
            //Check if the modules name equalIgnoreCases the module you are trying to effect
            if (module.getName().equalsIgnoreCase(modName)) {
                //Sets the module to the opposite of whatever its previous drawn state was
                module.setDrawn(!module.isDrawn());
                //sends a client side message to the player
                MessageUtil.sendMessage("Set " + module.getName() + " to " + (module.isDrawn() ? ChatFormatting.GREEN : ChatFormatting.RED) + " DRAWN" + ChatFormatting.WHITE + "!", false);
                //Breaks out of the loop and ends the command with a return
                return;
            }
        }
        //Catch for if the module name you inputted couldn't be found
        MessageUtil.sendMessage("Couldn't find the module you entered.", false);
    }
}
