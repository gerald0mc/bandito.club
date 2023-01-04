package club.bandito.client.module;

import club.bandito.client.Bandito;
import club.bandito.client.managers.config.IConfigurable;
import club.bandito.client.module.mods.hud.HUDModule;
import club.bandito.client.module.setting.*;
import club.bandito.client.util.MessageUtil;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public abstract class Module implements IConfigurable {

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting> settings;
    private int key;
    private boolean enabled;

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private boolean alwaysEnabled = false;
    private boolean drawn = true;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.settings = new ArrayList<>();
        this.enabled = false;
        this.key = Keyboard.KEY_NONE;
        Bandito.getConfigManager().getConfigs().add(this);
    }

    public void toggle() {
        if (enabled) disable(); else enable();
    }

    public void enable() {
        if (!enabled) {
            enabled = true;
            MinecraftForge.EVENT_BUS.register(this);
            if (!nullCheck()) {
                MessageUtil.sendMessage(getName() + " " + ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + "!", false, -Bandito.INSTANCE.hashCode());
            }
            onEnable();
        }
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            MinecraftForge.EVENT_BUS.unregister(this);
            if (!nullCheck()) {
                MessageUtil.sendMessage(getName() + " " + ChatFormatting.RED + "disabled" + ChatFormatting.RESET + "!", false, -Bandito.INSTANCE.hashCode());
            }
            onDisable();
        }
    }

    protected void onEnable() { }

    protected void onDisable() { }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setAlwaysEnabled(boolean alwaysEnabled) {
        this.alwaysEnabled = alwaysEnabled;
    }

    public boolean isAlwaysEnabled() {
        return alwaysEnabled;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public boolean isDrawn() {
        return drawn;
    }

    public String getMetaData() {
        return null;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public static boolean nullCheck() {
        return mc.world == null || mc.player == null;
    }

    @Override
    public void readFromJson(JsonObject jsonObject) {
        this.setKey(jsonObject.get("Keybind").getAsInt());
        if (jsonObject.get("Enabled").getAsBoolean()) enable();
        if (!jsonObject.get("Drawn").getAsBoolean()) setDrawn(false);

        if (category == Category.HUD) {
            JsonObject xAndYObject = jsonObject.get("XAndY").getAsJsonObject();
            HUDModule hudModule = (HUDModule) this;
            int x = xAndYObject.get("X").getAsInt();
            int y = xAndYObject.get("Y").getAsInt();
            hudModule.getComponent().x = x;
            hudModule.getComponent().y = y;
        }

        if (!jsonObject.has("Settings")) return;
        final JsonObject settingObject = jsonObject.get("Settings").getAsJsonObject();
        for (Setting setting : settings) {
            if (!settingObject.has(setting.getName()))
                continue;
            setting.loadConfig(settingObject);
        }
    }

    @Override
    public void writeToJson(JsonObject jsonObject) {
        jsonObject.addProperty("Name", getName());
        jsonObject.addProperty("Keybind", getKey());
        jsonObject.addProperty("Enabled", isEnabled());
        jsonObject.addProperty("Drawn", isDrawn());

        if (category == Category.HUD) {
            JsonObject xAndYObject = new JsonObject();
            HUDModule hudModule = (HUDModule) this;
            int x = hudModule.getComponent().x;
            int y = hudModule.getComponent().y;
            xAndYObject.addProperty("X", x);
            xAndYObject.addProperty("Y", y);
            jsonObject.add("XAndY", xAndYObject);
        }

        final JsonObject settingObject = new JsonObject();
        for (Setting s : settings) {
            s.saveConfig(settingObject);
        }
        jsonObject.add("Settings", settingObject);
    }

    public enum Category {
        COMBAT("Combat"),
        RENDER("Render"),
        MOVEMENT("Movement"),
        WORLD("World"),
        MISC("Misc"),
        CLIENT("Client"),
        HUD("Hud");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
