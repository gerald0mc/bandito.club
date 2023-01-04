package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.mods.misc.Reminders;
import club.bandito.client.util.MessageUtil;

public class Reminder extends Command {
    public Reminder() {
        super("Reminder", "Sets a reminder that shows up on screen.", new String[] {"reminder", "[minutes]", "[message]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        //arg check
        //First one is checking if you only inputted one arg, while the other is checking if you only inputted two
        if (args.length == 1) {
            MessageUtil.sendMessage("Please input the minutes and message you wish to set for the reminder.", false);
            return;
        } else if (args.length == 2) {
            MessageUtil.sendMessage("Please input the message you wish to set for the reminder.", false);
            return;
        }
        //Empty variable being created
        float minutes;
        //Try to set this variable
        try {
            //Try to parse the value from the string you inputted
            minutes = Float.parseFloat(args[1]);
        } catch (Exception e) {
            //Catch if you didn't input something correctly
            MessageUtil.sendMessage("The minute value you put in didn't work.", false);
            return;
        }
        //Catch if your value wasn't caught acting up by the catch
        if (minutes == -1) {
            MessageUtil.sendMessage("The minute value you put in didn't work.", false);
            return;
        }
        //Empty string variable
        String message = "";
        //Loop through the length of the message to get the reminder message
        for (int i = 2; i < args.length; i++) {
            //Grab the message from the space in the string array
            String s = args[i];
            // if (i == 0 || i == 1) continue;
            //If the int is not the end of the array, else place a space
            if (i == args.length - 1)
                message += s;
            else
                message += s + " ";
        }
        //Get the module with a method, then add a reminder to the reminders hashmap
        Bandito.getModuleManager().getModule(Reminders.class).reminders.put(new Reminders.Reminder(message, minutes), System.currentTimeMillis());
        //Send a client side message to the player
        MessageUtil.sendMessage("Added new reminder to reminders. [" + message + "] [" + minutes + "]", false);
    }
}
