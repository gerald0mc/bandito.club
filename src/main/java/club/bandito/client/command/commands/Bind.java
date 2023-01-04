package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.Module;
import club.bandito.client.util.MessageUtil;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("Bind", "Binds a module to a new key.", new String[] {"bind", "[module]", "[key]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //Catch if you don't have enough words inputted
        if (args.length < 3) {
            MessageUtil.sendMessage("Please specify the module and bind you wish to set the module to.", false);
            return;
        //Catch if you too many words inputted    
        } else if (args.length > 3) {
            MessageUtil.sendMessage("Too many words, should only be [bind module key].", false);
            return;
        }
        //Gets the second word you sent in your command
        String moduleName = args[1];
        //Checks if the key you have entered is actually a key or not
        if (Keyboard.getKeyIndex(args[2].toUpperCase()) == Keyboard.KEY_NONE) {
            MessageUtil.sendMessage("Something went wrong with your key you entered.", false);
            return;
        }
        //Loop through all of the modules in the client
        for (Module module : Bandito.getModuleManager().getModules()) {
            //Checks if the current looped module's name equalsIgnoreCase the moduleName
            if (module.getName().equalsIgnoreCase(moduleName)) {
                //Sets the modules bind to the key you entered
                module.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
                //Sends a message client side to the player
                MessageUtil.sendMessage("Set " + module.getName() + "s bind to " + Keyboard.getKeyName(module.getKey()) + ".", false);
                return;
            }
        }
        //Catch if the client doesn't contain that module
        MessageUtil.sendMessage("Couldn't find the module you put in sorry.", false);
    }
}
