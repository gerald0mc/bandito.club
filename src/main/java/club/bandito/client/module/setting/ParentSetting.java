package club.bandito.client.module.setting;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;

public class ParentSetting extends Setting {
    private boolean value = false;

    public ParentSetting(String name) {
        super(name);
    }
    
    public ParentSetting(String name, ParentSetting parent) {
        super(name, parent);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) { }

    @Override
    public void loadConfig(JsonObject jsonObject) { }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [Parent] " + "\n");
    }
}
