package club.bandito.client.command.commands;

import club.bandito.client.Bandito;
import club.bandito.client.command.Command;
import club.bandito.client.module.mods.world.AutoTrader;
import club.bandito.client.util.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

public class Trade extends Command {
    public Trade() {
        super("Trade", "The command used to add custom trades to AutoTrader.", new String[] {"trade", "[add/clear/list]", "[(Selling Item Name)/(Optional) Count/(Optional) (Second Selling Item Name)/(Optional) Count/(Buying Item Name)]"});
    }

    @Override
    public void onCommand(String[] args) {
        //Gets the AutoTrader module and casts it to module
        AutoTrader module = Bandito.getModuleManager().getModule(AutoTrader.class);
        //Checks if the command entered is the right amount of args or not
        if (args.length == 1) {
            MessageUtil.sendMessage("Please specify the add or clear subcommand.", false);
            return;
        }
        //A switch for all sub-commands of the command
        switch (args[1]) {
            //Clear sub-command, clears the customTrades list    
            case "clear":
                //Gets the customTrades list from the AutoTrader module
                List<AutoTrader.Trade> list = Bandito.getModuleManager().getModule(AutoTrader.class).customTrades;
                //Catches if the customTrades list is already empty
                if (list.isEmpty()) {
                    MessageUtil.sendMessage("Custom trade list is already empty can't clear.", false);    
                } else {
                    //Clear the list
                    list.clear();
                    //Send a message client side to the player
                    MessageUtil.sendMessage("Cleared custom trade list.", false);
                }
                return;
            //List sub-command, used to list all the trades you already have added    
            case "list":
                //Gets the customTrades list from the AutoTrader module
                List<AutoTrader.Trade> tradeList = Bandito.getModuleManager().getModule(AutoTrader.class).customTrades;
                //Catches if the customTrades list is empty
                if (tradeList.isEmpty()) {
                    MessageUtil.sendMessage("Custom trade list is empty nothing to list.", false);
                } else {
                    //Starting message
                    MessageUtil.sendMessage("AutoTrader Custom Trades: ", true);
                    //Loop through all of the customTrades list
                    for (AutoTrader.Trade trade : tradeList) {
                        //Get the first item the player is selling to the villagers name
                        String sellingOneName = trade.getSellingItemOne().getItemStackDisplayName(new ItemStack(trade.getSellingItemOne()));
                        //Get the second item the player is selling to the villagers name
                        String sellingTwoName = trade.getSellingItemTwo().getItemStackDisplayName(new ItemStack(trade.getSellingItemTwo()));
                        //Get the item the villager is selling to the players name
                        String buyingName = trade.getBuyingItem().getItemStackDisplayName(new ItemStack(trade.getBuyingItem()));
                        //Sends a client side message telling you that info (EX: Buying: Rotten Flesh Max 32x Gold Max 16x Selling: Emerald
                        MessageUtil.sendMessage(GREEN + "Buying: " + GRAY + sellingOneName + GREEN + " Max " + GRAY + "x" + trade.getMaxOne() + (trade.getSellingItemTwo() != Items.AIR ? " " + sellingTwoName + GREEN + " Max " + GRAY + "x" + trade.getMaxTwo() : "") + GREEN + " Selling: " + GRAY + buyingName, true);
                    }
                }
                return;
            //Add sub-command, used to add trades to the customTrades list    
            case "add":
                //Checks if the args are long enough for this command
                if (args.length < 4) {
                    MessageUtil.sendMessage("Please input a correct trade format. [Selling Item Name/(Optional) Count/(Optional) Second Selling Item Name/(Optional) Count/Buying Item Name]", false);
                    return;
                }
                //If the length of the args is 4 words
                if (args.length == 4) {
                    //Get this value from the third arg
                    String sellingItemName = String.join(" ", args[2].split("_"));
                    //Empty Item variable
                    Item itemOne = Items.AIR;
                    //Get this value from the fourth arg
                    String buyingItemName = String.join(" ", args[3].split("_"));
                    //Empty item variable
                    Item itemTwo = Items.AIR;
                    //Loop through all the item in Minecraft
                    for (Item item : Item.REGISTRY) {
                        //Checks if the displayName of the currently looped through item is equal to the sellingItemName
                        if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(sellingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            itemOne = item;
                        //Checks if the displayName of the currently looped through item is equal to the buyingItemName    
                        } else if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(buyingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            itemTwo = item;
                        }
                    }
                    //Checks if the process went through successfully 
                    if (!itemOne.equals(Items.AIR) && !itemTwo.equals(Items.AIR)) {
                        //Add the trade to the customTrades list
                        module.customTrades.add(new AutoTrader.Trade(itemOne, itemTwo));
                        //Send a client side message
                        MessageUtil.sendMessage("Added new trade to custom trade list.", false);
                        return;
                    }
                    //Catch if the process failed
                    MessageUtil.sendMessage("Couldn't add trade to trade list.", false);
                } else if (args.length == 5) {
                    //Get this value from the third arg
                    String sellingItemName = String.join(" ", args[2].split("_"));
                    //Empty item variable
                    Item itemOne = Items.AIR;
                    //Get this value from the fourth arg
                    String secondSellingItemName = String.join(" ", args[3].split("_"));
                    //Empty item variable
                    Item itemTwo = Items.AIR;
                    //Get this value from the fifth arg
                    String buyingItemName = String.join(" ", args[4].split("_"));
                    //Empty item variable
                    Item sellingItem = Items.AIR;
                    //Loop through all the items in Minecraft
                    for (Item item : Item.REGISTRY) {
                        //Checks if the displayName of the currently looped through item is equal to the sellingItemName
                        if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(sellingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            itemOne = item;
                        //Checks if the displayName of the currently looped through item is equal to the secondSellingItemName    
                        } else if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(secondSellingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            itemTwo = item;
                        //Checks if the displayName of the currently looped through item is equal to the buyingItemName    
                        } else if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(buyingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            sellingItem = item;
                        }
                    }
                    //Checks if the process went through successfully
                    if (!itemOne.equals(Items.AIR) && !itemTwo.equals(Items.AIR) && !sellingItem.equals(Items.AIR)) {
                        //Adds the trade to the customTrades list
                        module.customTrades.add(new AutoTrader.Trade(itemOne, itemTwo, sellingItem));
                        //Sends a client side message
                        MessageUtil.sendMessage("Added new trade to custom trade list.", false);
                        return;
                    }
                    //Catch if the process failed
                    MessageUtil.sendMessage("Couldn't add trade to trade list.", false);
                } else if (args.length == 7) {
                    //Get this value from the third arg
                    String sellingItemName = String.join(" ", args[2].split("_"));
                    //Empty int variable
                    int itemOneMax = -1;
                    //Try to parse the string to the int
                    try {
                        itemOneMax = Integer.parseInt(args[3]);
                    } catch (Exception ignored) {}
                    //If the try fails
                    if (itemOneMax == -1) {
                        MessageUtil.sendMessage("The number you inputed for the maximum isn't a valid number.", false);
                    }
                    //Empty item variable
                    Item itemOne = Items.AIR;
                    //Get this value from the fifth arg
                    String secondSellingItemName = String.join(" ", args[4].split("_"));
                    //Empty int variable
                    int itemTwoMax = -1;
                    //Try to parse the string to the int
                    try {
                        itemTwoMax = Integer.parseInt(args[5]);
                    } catch (Exception ignored) {}
                    //If the try fails
                    if (itemTwoMax == -1) {
                        MessageUtil.sendMessage("The number you inputed for the maximum isn't a valid number.", false);
                    }
                    //Empty item variable
                    Item itemTwo = Items.AIR;
                    //Get this value from the seventh arg
                    String buyingItemName = String.join(" ", args[6].split("_"));
                    //Empty item variable
                    Item sellingItem = Items.AIR;
                    //Loop through all the items in Minecraft
                    for (Item item : Item.REGISTRY) {
                        //Checks if the displayName of the currently looped through item is equal to the sellingItemName
                        if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(sellingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            itemOne = item;
                        //Checks if the displayName of the currently looped through item is equal to the secondSellingItemName    
                        } else if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(secondSellingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            itemTwo = item;
                        //Checks if the displayName of the currently looped through item is equal to the buyingItemName    
                        } else if (item.getItemStackDisplayName(new ItemStack(item)).equalsIgnoreCase(buyingItemName)) {
                            //Set the empty item variable to the currently looped through item
                            sellingItem = item;
                        }
                    }
                    //Check if the process went through successfully
                    if (!itemOne.equals(Items.AIR) && !itemTwo.equals(Items.AIR) && !sellingItem.equals(Items.AIR)) {
                        //Adds the trade to the customTrades list
                        module.customTrades.add(new AutoTrader.Trade(itemOne,  itemOneMax, itemTwo, itemTwoMax, sellingItem));
                        //Sends a client side message
                        MessageUtil.sendMessage("Added new trade to custom trade list.", false);
                        return;
                    }
                    //Catch if the process failed
                    MessageUtil.sendMessage("Couldn't add trade to trade list.", false);
                }
        }
    }
}
