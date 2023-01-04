package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.*;
import club.bandito.client.util.MessageUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ShowOff extends Command {
    public ShowOff() {
        super("ShowOff", "Creates a txt with all of the modules and commands in it.", new String[] {"showoff"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        try {
            //Create new instance of the ShowOff.txt file
            File file = new File(Bandito.getConfigManager().getMiscDir(), "ShowOff.txt");
            //If the file exists, delete it then make a new one
            if (file.exists()) {
                file.delete();
            }
            //Create new file
            file.createNewFile();
            //Create new instance of the BufferedWriter with the previous file as the passed in variable file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            //Write module category
            writer.write("==========MODULES========== \n");
            //Loop through all the modules in the client
            for (Module module : Bandito.getModuleManager().getModules()) {
                //Writes the modules name, category, and description (EX: ESP [Render]: Renders entities.)
                writer.write(module.getName() + " [" + module.getCategory() + "]: " + module.getDescription() + (!module.getSettings().isEmpty() ? " {" : "") + "\n");
                //Checks if the current module in the loop has settings and if not continue through the loop
                if (module.getSettings().isEmpty()) continue;
                //Loop through all the settings in the module
                for (Setting s : module.getSettings()) {
                    //Abstract method found in Setting.java
                    s.saveShowoff(writer);
                }
                //Close the setting box
                writer.write("} \n");
            }
            //Write command category
            writer.write("==========COMMANDS========== \n");
            //Loop through all the commands in the client
            for (Command command : Bandito.getCommandManager().commandList) {
                //Writes the commands name, usage args, and description (EX: Help [help]: Sends the help message in chat.)
                writer.write(command.getName() + " " + Arrays.asList(command.getUsage()) + ": " + command.getDescription() + "\n");
            }
            //Flush the writer
            writer.flush();
            //Close the writer
            writer.close();
            //Send client side message to the player
            MessageUtil.sendMessage("Finished creating ShowOff.txt, you can find it in .minecraft/Bandito/Misc/ShowOff.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
