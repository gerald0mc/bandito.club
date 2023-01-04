package club.bandito.client.module.setting.customsetting;

import club.bandito.client.module.setting.ParentSetting;
import club.bandito.client.module.setting.Setting;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;

public class CustomActionSetting extends Setting {
    private Action action;

    public CustomActionSetting(String name, Action action) {
        super(name);
        this.action = action;
    }

    public CustomActionSetting(String name, Action action, ParentSetting parent) {
        super(name, parent);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public String getUsage() {
        return null;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) { }

    @Override
    public void loadConfig(JsonObject jsonObject) { }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [CustomAction]" + (getUsage() != null ? ": " + getUsage() : "") + "\n");
    }
}
