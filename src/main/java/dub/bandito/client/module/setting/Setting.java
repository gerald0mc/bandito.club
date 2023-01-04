package club.bandito.client.module.setting;

import com.google.gson.JsonObject;

import club.bandito.client.module.setting.ParentSetting;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class Setting {

    private final String name;
    private final ParentSetting parent;

    public Setting(String name) {
        this.name = name;
        this.parent = null;
    }
    
    public Setting(String name, ParentSetting parent) {
        this.name = name;
        this.parent = parent;
    }    

    public String getName() {
        return name;
    }
    
    public ParentSetting getParent() {
        return parent;
    }

    public abstract void saveConfig(JsonObject jsonObject);

    public abstract void loadConfig(JsonObject jsonObject);

    public abstract void saveShowoff(BufferedWriter bufferedWriter) throws IOException;
}
