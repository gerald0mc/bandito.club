package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.Module;
import club.bandito.client.util.MessageUtil;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggles a module.", new String[] {"toggle", "[module]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //Two seperate arg length checks
        //One checks if it is only a word long, and then other one checks if the arg length is more then two long
        if (args.length == 1) {
            MessageUtil.sendMessage("Please specify which module you would like to toggle.", false);
            return;
        } else if (args.length > 2) {
            MessageUtil.sendMessage("Too many words, should only be [toggle moduleName].", false);
            return;
        }
        //Gets the string variable from the second arg
        String moduleName = args[1];
        //Loops through all the modules in the client
        for (Module module : Bandito.getModuleManager().getModules()) {
            //Gets if the currently looped through modules name is the arg you entere or not
            if (module.getName().equalsIgnoreCase(moduleName)) {
                //Toggles the module if so
                module.toggle();
                //Then breaks the for loop with a return
                return;
            }
        }
        //Catch if you didn't input a arg that is findable
        MessageUtil.sendMessage("Couldn't find the module you put in sorry.", false);
    }
}
