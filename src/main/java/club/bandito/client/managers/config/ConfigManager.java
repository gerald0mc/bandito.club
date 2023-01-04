package club.bandito.client.managers.config;

import club.bandito.client.Bandito;
import club.bandito.client.module.mods.world.AutoTrader;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {

    private final Gson gson;
    private final JsonParser parser;
    //.minecraft/Bandito
    private final File saveDir;
    //.minecraft/Bandito/Config
    private final File configDir;
    //.minecraft/Bandito/Misc
    private final File miscDir;
    //.minecraft/Bandito/Misc/CustomTrades.json
    private final File customTradeFile;
    //.minecraft/Bandito/Misc/Exported Chats/*
    private final File exportedChatFile;
    //All the module configs
    private final Set<IConfigurable> configs;

    public ConfigManager() {
        this.saveDir = new File(Minecraft.getMinecraft().gameDir, Bandito.MOD_NAME);
        this.configDir = new File(saveDir, "Config");
        this.miscDir = new File(saveDir, "Misc");
        this.customTradeFile = new File(miscDir, "CustomTrades.json");
        this.exportedChatFile = new File(miscDir, "Exported Chats");
        this.configs = new HashSet<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.parser = new JsonParser();

        //Creating folders and files
        if (!saveDir.exists())
            saveDir.mkdirs();
        if (!configDir.exists())
            configDir.mkdir();
        if (!miscDir.exists())
            miscDir.mkdir();
        if (!customTradeFile.exists()) {
            try {
                customTradeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!exportedChatFile.exists()) {
            exportedChatFile.mkdir();
        }
    }

    public void saveAll() {
        saveTrades();
        for (IConfigurable config : configs) {
            final JsonObject jsonObject = new JsonObject();
            //Abstract method found in Module.java
            config.writeToJson(jsonObject);

            try {
                final File file = new File(configDir, config.getName() + ".json");
                if (!file.exists())
                    file.createNewFile();

                final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

                //Converts the JsonObject containing the config into the module config json file
                gson.toJson(jsonObject, bufferedWriter);

                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (Exception e) {
                System.out.println("[Bandito] Error saving " + config.getName() + ": " + e.getMessage());
            }
        }
    }

    public void loadAll() {
        for (IConfigurable config : configs) {
            try {
                final File file = new File(configDir, config.getName() + ".json");
                if (!file.exists())
                    continue;
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
                //Abstract method found in Module.java
                config.readFromJson(jsonObject);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[Bandito] Error loading " + config.getName() + ": " + e.getMessage());
            }
        }
        loadTrades();
    }

    public File getSaveDir() {
        return saveDir;
    }

    public File getConfigDir() {
        return configDir;
    }

    public File getMiscDir() {
        return miscDir;
    }

    public File getExportedChatFile() {
        return exportedChatFile;
    }

    public Set<IConfigurable> getConfigs() {
        return configs;
    }

    public void loadTrades() {
        try {
            //Check if the file is empty
            if (customTradeFile.length() == 0) return;
            final BufferedReader reader = new BufferedReader(new FileReader(customTradeFile));
            final AutoTrader module = Bandito.getModuleManager().getModule(AutoTrader.class);
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            //Loop through the object to get all the trades
            for (int i = 0; i < jsonObject.size(); i++) {
                //Trades are saved as Trade1, Trade2, Trade3, etc...
                JsonObject tradeOffer = jsonObject.get("Trade" + i).getAsJsonObject();
                //Just a catch
                if (tradeOffer == null) break;
                //Gets the first item you are selling
                final JsonObject itemOneObject = tradeOffer.get("ItemOne").getAsJsonObject();
                final String itemOneName = itemOneObject.get("Name").getAsString();
                Item itemOne = Items.AIR;
                int maxOne = itemOneObject.get("Count").getAsInt();
                //Gets the second item you are selling
                final JsonObject itemTwoObject = tradeOffer.get("ItemTwo").getAsJsonObject();
                final String itemTwoName = itemTwoObject.get("Name").getAsString();       
                Item itemTwo = Items.AIR;
                int maxTwo = itemTwoObject.get("Count").getAsInt();
                //Gets the item you are buying
                final JsonObject buyingItemObject = tradeOffer.get("ItemBuying").getAsJsonObject();
                final String buyingItemName = buyingItemObject.get("Name").getAsString();
                Item buyingItem = Items.AIR;
                //Loop through all items in the game to find the items
                for (Item item : Item.REGISTRY) {
                    if (item.getItemStackDisplayName(new ItemStack(item)).equals(itemOneName)) {
                        itemOne = item;
                    } else if (item.getItemStackDisplayName(new ItemStack(item)).equals(itemTwoName)) {
                        itemTwo = item;
                    } else if (item.getItemStackDisplayName(new ItemStack(item)).equals(buyingItemName)) {
                        buyingItem = item;
                    }
                }
                //Add the trade to the trade list.
                module.customTrades.add(new AutoTrader.Trade(itemOne, maxOne, itemTwo, maxTwo, buyingItem));
            }
            System.out.println("[Bandito] Finished loading custom trades.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTrades() {
        AutoTrader autoTrader = Bandito.getModuleManager().getModule(AutoTrader.class);
        //Check if the trade list is empty or not
        if (!autoTrader.customTrades.isEmpty()) {
            try {
                if (customTradeFile.exists()) {
                    customTradeFile.delete();
                    customTradeFile.createNewFile();
                }
                final BufferedWriter writer = new BufferedWriter(new FileWriter(customTradeFile));
                final JsonObject mainObject = new JsonObject();
                //Loop through all of the trades in the trade list
                for (int i = 0; i < autoTrader.customTrades.size(); i++) {
                    AutoTrader.Trade trade = autoTrader.customTrades.get(i);
                    JsonObject jsonObject = new JsonObject();
                    //First Item
                    JsonObject itemOneObject = new JsonObject();
                    itemOneObject.addProperty("Name", trade.getSellingItemOne().getItemStackDisplayName(new ItemStack(trade.getSellingItemOne())));
                    itemOneObject.addProperty("Count", trade.getMaxOne());
                    jsonObject.add("ItemOne", itemOneObject);
                    //Second Item
                    JsonObject itemTwoObject = new JsonObject();
                    itemTwoObject.addProperty("Name", trade.getSellingItemTwo().getItemStackDisplayName(new ItemStack(trade.getSellingItemTwo())));
                    itemTwoObject.addProperty("Count", trade.getMaxTwo());
                    jsonObject.add("ItemTwo", itemTwoObject);
                    //Buying Item
                    JsonObject itemBuyingObject = new JsonObject();
                    itemBuyingObject.addProperty("Name", trade.getBuyingItem().getItemStackDisplayName(new ItemStack(trade.getBuyingItem())));
                    jsonObject.add("ItemBuying", itemBuyingObject);
                    //Adding to main object.
                    mainObject.add("Trade" + i, jsonObject);
                }
                //Writes the JsonObject we have created to the json file.
                gson.toJson(mainObject, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
