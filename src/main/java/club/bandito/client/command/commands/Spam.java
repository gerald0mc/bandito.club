package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.mods.misc.Spammer;
import club.bandito.client.util.MessageUtil;

public class Spam extends Command {
    public Spam() {
        super("Spam", "Command for the spam module.", new String[] {"spammer"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //arg length check
        if (args.length != 1) {
            MessageUtil.sendMessage("You have inputted more words then is needed please perform the " + Bandito.getCommandManager().PREFIX + "commands.", false);
            return;
        }
        //Gets the module, then performs a method found within the module
        Bandito.getModuleManager().getModule(Spammer.class).reloadSpammer();
    }
}
