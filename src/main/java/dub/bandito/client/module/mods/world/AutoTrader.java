package club.bandito.client.module.mods.world;

import club.bandito.client.event.PlayerLivingTickEvent;
import club.bandito.client.mixin.IGuiMerchant;
import club.bandito.client.module.Module;
import club.bandito.client.module.setting.*;
import club.bandito.client.util.MessageUtil;
import club.bandito.client.util.Timer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

/**
 * @author gerald0mc
 * @since 7/8/22
 * #TODO ADD PAGE SWITCH
 */
public class AutoTrader extends Module {
    public AutoTrader() {
        super("AutoTrader", "Trades for you.", Category.WORLD);
    }

    private final FloatSetting delay = new FloatSetting("Delay", 250, 0, 1000);
    private final BooleanSetting tradeMessage = new BooleanSetting("TradeMessage", true);
    private final BooleanSetting messages = new BooleanSetting("DebugMessages", false);

    private final ListSetting items = new ListSetting("Items", new Setting[] {
            new BooleanSetting("Potato", false),
            new BooleanSetting("Carrot", false),
            new BooleanSetting("Wheat", false),
            new BooleanSetting("Pumpkin", false),
            new BooleanSetting("Melon", false),
            new BooleanSetting("String", false),
            new BooleanSetting("White Wool", false),
            new BooleanSetting("Paper", false),
            new BooleanSetting("Coal", false),
            new BooleanSetting("Iron", false),
            new BooleanSetting("Diamond", false),
            new BooleanSetting("Leather", false),
            new BooleanSetting("Raw PorkChop", false),
            new BooleanSetting("Raw Chicken", false),
            new BooleanSetting("Rotten Flesh", false),
            new BooleanSetting("Gold Ingot", false)
    });

    //List of trades you can add with the Trade command
    public List<Trade> customTrades = new LinkedList<>();
    //The MerchantRecipeList that is set when you open a villagers GUI
    private MerchantRecipeList recipeList = null;
    private Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(PlayerLivingTickEvent event) {
        if (nullCheck()) return;
        //Check if you are in the Trade GUI and set the recipeList to null if so
        if (!(mc.currentScreen instanceof GuiMerchant) && recipeList != null)
            recipeList = null;
        //Check if you are in the Trade GUI and set the recipeList to the villagers RecipeList
        if (mc.currentScreen instanceof GuiMerchant && recipeList == null)
            recipeList = ((GuiMerchant) mc.currentScreen).getMerchant().getRecipes(mc.player);
        //Check if the recipeList is set to null so you are not trying to trade at random
        if (recipeList == null)
            return;
        GuiMerchant gui = (GuiMerchant) mc.currentScreen;
        //Loop through all of the trades in the RecipeList
        for (int i = 0; i < recipeList.size(); i++) {
            MerchantRecipe recipe = recipeList.get(i);
            //Check if the recipe allows you to trade or not and continue if so
            if (recipe.isRecipeDisabled())
                continue;
            //Check if the current page you are on is the current recipe
            if (i != ((IGuiMerchant) gui).getSelectedMerchantRecipe())
                continue;
            //Check if the correct amount of time has passed
            if (!timer.passed((long) delay.getValue()))
                return;
            //Get all of the trades from the customTrades list
            List<Trade> tradingItems = new LinkedList<>(customTrades);
            //Loop through all the settings in the Items setting
            for (Setting setting : items.getList()) {
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting) setting;
                    if (booleanSetting.getValue())
                        tradingItems.add(new Trade(getTradeItem(setting.getName()), Items.EMERALD));
                }
            }
            //Checks if you have no trades either enabled or in your customTrades list
            if (tradingItems.isEmpty()) {
                MessageUtil.sendMessage("Please enable some trades in the AutoTrader settings.", false);
                toggle();
                return;
            }
            boolean debugMessages = messages.getValue();
            //Loop through the tradingItems
            for (Trade item : tradingItems) {
                //Make sure the current looped trade is the correct one for you
                if (item.getSellingItemOne().equals(recipe.getItemToBuy().getItem()) && item.getSellingItemTwo().equals(recipe.getSecondItemToBuy().getItem())) {
                    //Checks if your current count is below the required count and will return if so
                    if (recipe.getItemToBuy().getCount() > item.getMaxOne() || recipe.getSecondItemToBuy().getCount() > item.getMaxTwo())
                        return;
                    //Checks if the item in the bought slot is equal to the item you are buying
                    if (mc.player.openContainer.getSlot(2).getStack().getItem().equals(item.getBuyingItem())) {
                        //Moves the item to your inventory
                        mc.playerController.windowClick(gui.inventorySlots.windowId, 2, 0, ClickType.QUICK_MOVE, mc.player);
                        if (debugMessages)
                            MessageUtil.sendMessage("Took emeralds out.", false);
                    }
                    //Checks if the first slot you are buying has a count less then what you need to buy it
                    if (mc.player.openContainer.getSlot(0).getStack().getCount() < recipe.getItemToBuy().getCount()) {
                        int slot = -1;
                        for (int i1 = 3; i1 < 38; i1++) {
                            ItemStack stack = mc.player.openContainer.getSlot(i1).getStack();
                            if (stack.getItem().equals(item.getSellingItemOne()))
                                slot = i1;
                        }
                        if (slot != -1 && !recipe.isRecipeDisabled()) {
                            mc.playerController.windowClick(gui.inventorySlots.windowId, slot, 0, ClickType.PICKUP, mc.player);
                            mc.playerController.windowClick(gui.inventorySlots.windowId, 0, 0, ClickType.PICKUP, mc.player);
                            mc.playerController.windowClick(gui.inventorySlots.windowId, slot, 0, ClickType.PICKUP, mc.player);
                            if (debugMessages)
                                MessageUtil.sendMessage("Added item to merchant menu.", false);
                        }
                    //Checks if the second slot you are buying has a count less then what you need to buy it, and that the item is not equal to null that you are buying    
                    } else if (mc.player.openContainer.getSlot(1).getStack().getCount() < recipe.getSecondItemToBuy().getCount() && !item.getSellingItemTwo().equals(Items.AIR)) {
                        int slot = -1;
                        for (int i1 = 3; i1 < 38; i1++) {
                            ItemStack stack = mc.player.openContainer.getSlot(i1).getStack();
                            if (stack.getItem().equals(item.getSellingItemTwo()))
                                slot = i1;
                        }
                        if (slot != -1 && !recipe.isRecipeDisabled()) {
                            mc.playerController.windowClick(gui.inventorySlots.windowId, slot, 0, ClickType.PICKUP, mc.player);
                            mc.playerController.windowClick(gui.inventorySlots.windowId, 1, 0, ClickType.PICKUP, mc.player);
                            if (debugMessages)
                                MessageUtil.sendMessage("Added item to merchant menu.", false);
                        }
                    //Checks if the item in the first is ready to be bought    
                    } else if (mc.player.openContainer.getSlot(0).getStack().getCount() >= recipe.getItemToBuy().getCount() && !recipe.isRecipeDisabled()) {
                        //Sends the packet needed to process a trade
                        mc.player.connection.sendPacket(new CPacketConfirmTransaction(gui.inventorySlots.windowId, gui.inventorySlots.getNextTransactionID(mc.player.inventory), true));
                        if (debugMessages)
                            MessageUtil.sendMessage("Sent transaction packet.", false);
                        if (tradeMessage.getValue())
                            MessageUtil.sendMessage(ChatFormatting.GREEN + "Performed trade.", false);
                    }
                    //Reset the delay timer
                    timer.reset();
                }
            }
        }
    }
    
    public Item getTradeItem(String setting) {
        for (Item item : Item.REGISTRY) {
            if (item.getItemStackDisplayName(new ItemStack(item)).equals(setting)) {
                return item;
            }
        }
        return Items.AIR;
    }

    public static class Trade {
        private final Item sellingItemOne;
        private int maxOne = 64;
        private Item sellingItemTwo = Items.AIR;
        private int maxTwo = 64;
        private final Item buyingItem;

        public Trade(Item buyingItem, Item sellingItem) {
            this.sellingItemOne = buyingItem;
            this.buyingItem = sellingItem;
        }

        public Trade(Item buyingItem, Item secondBuyingItem, Item sellingItem) {
            this.sellingItemOne = buyingItem;
            this.sellingItemTwo = secondBuyingItem;
            this.buyingItem = sellingItem;
        }

        public Trade(Item buyingItem, int max, Item secondBuyingItem, Item sellingItem) {
            this.sellingItemOne = buyingItem;
            this.maxOne = max;
            this.sellingItemTwo = secondBuyingItem;
            this.maxTwo = 64;
            this.buyingItem = sellingItem;
        }

        public Trade(Item buyingItem, int max, Item secondBuyingItem, int maxTwo, Item sellingItem) {
            this.sellingItemOne = buyingItem;
            this.maxOne = max;
            this.sellingItemTwo = secondBuyingItem;
            this.maxTwo = maxTwo;
            this.buyingItem = sellingItem;
        }

        public Item getSellingItemOne() {
            return sellingItemOne;
        }

        public int getMaxOne() {
            return maxOne;
        }

        public Item getSellingItemTwo() {
            return sellingItemTwo;
        }

        public int getMaxTwo() {
            return maxTwo;
        }

        public Item getBuyingItem() {
            return buyingItem;
        }
    }
}
