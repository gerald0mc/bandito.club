package club.bandito.client.command.commands;

import club.bandito.client.command.Command;
import club.bandito.client.util.MessageUtil;
import net.minecraft.client.Minecraft;

public class FOV extends Command {
    public FOV() {
        super("FOV", "Allows you to change your field of view.", new String[] {"fov", "[value]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //arg length check
        //The first one checks if you didn't input the number for the FOV you want, while the second check is for if you inputted over the max of two numbers
        if (args.length == 1) {
            MessageUtil.sendMessage("Please input the FOV you wish to set to.", false);
            return;
        } else if (args.length > 2) {
            MessageUtil.sendMessage("Over the maximum amount of words for this command.", false);
            return;
        }
        //Empty float variable
        float value;
        //Try to set the newelly created value to a parsed value
        try {
            value = Float.parseFloat(args[1]);
        } catch (Exception e) {
            //Catch for if that fails
            MessageUtil.sendMessage("Something went wrong, you probably didn't put a number.", false);
            return;
        }
        //Second catch for if that fails
        if (value == -1f) {
            MessageUtil.sendMessage("Something went wrong, you probably didn't put a number.", false);
            return;
        }
        //Set the players FOV to the parsed float
        Minecraft.getMinecraft().gameSettings.fovSetting = value;
        //Send a client side message to the player
        MessageUtil.sendMessage("Set FOV to " + value + ".", false);
    }
}
