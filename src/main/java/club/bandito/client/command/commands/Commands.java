package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.util.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.Arrays;

public class Commands extends Command {
    public Commands() {
        super("Commands", "Lists all commands in the client.", new String[] {"commands"});
    }

    @Override
    public void onCommand(String[] args) {
        //Check for if you entered the right amount of args
        if (args.length > 1) {
            MessageUtil.sendMessage("This command is only performed with [commands].", false);
            return;
        }
        //Sends the opening message
        MessageUtil.sendMessage("Bandito.club Commands:", true);
        //Loop through all of the commands in the client
        for (Command command : Bandito.getCommandManager().commandList) {
            //Sends a client side message with info about the currently looped through command (EX: Help [help]: Lists all commands in the client.)
            MessageUtil.sendMessage(ChatFormatting.GREEN + command.getName() + " " + ChatFormatting.WHITE + Arrays.toString(command.getUsage()) + ": " + ChatFormatting.GRAY + command.getDescription(), true);
        }
    }
}
