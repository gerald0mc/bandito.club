package club.bandito.client;

import club.bandito.client.event.listeners.HUDRenderListener;
import club.bandito.client.event.listeners.ModuleListener;
import club.bandito.client.event.listeners.PopListener;
import club.bandito.client.gui.ClickGuiScreen;
import club.bandito.client.managers.CommandManager;
import club.bandito.client.managers.ModuleManager;
import club.bandito.client.managers.config.ConfigManager;
import club.bandito.client.managers.notifications.NotificationManager;
import club.bandito.client.util.ProjectionUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
        modid = Bandito.MOD_ID,
        name = Bandito.MOD_NAME,
        version = Bandito.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class Bandito {

    public static final String MOD_ID = "bandito";
    public static final String MOD_NAME = "bandito.club";
    public static final String VERSION = "0.1";

    @Mod.Instance(MOD_ID)
    public static Bandito INSTANCE;

    //MAIN MANAGERS
    private ConfigManager configManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ClickGuiScreen clickGui;
    //SECONDARY MANAGERS
    private NotificationManager notificationManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //MAIN STUFF
        this.configManager = new ConfigManager();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.clickGui = new ClickGuiScreen();
        //SECONDARY STUFF
        this.notificationManager = new NotificationManager();
        //LISTENERS
        PopListener popListener = new PopListener();
        HUDRenderListener hudRenderListener = new HUDRenderListener();
        ModuleListener moduleListener = new ModuleListener();

        configManager.loadAll();
        Runtime.getRuntime().addShutdownHook(new Thread(configManager::saveAll));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        ProjectionUtil.updateMatrix();
    }

    public static ConfigManager getConfigManager() {
        return INSTANCE.configManager;
    }

    public static ModuleManager getModuleManager() {
        return INSTANCE.moduleManager;
    }

    public static CommandManager getCommandManager() {
        return INSTANCE.commandManager;
    }

    public static ClickGuiScreen getClickGui() {
        return INSTANCE.clickGui;
    }

    public static NotificationManager getNotificationManager() {
        return INSTANCE.notificationManager;
    }
}
