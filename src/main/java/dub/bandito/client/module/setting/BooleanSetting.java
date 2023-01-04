package club.bandito.client.module.setting;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;

public class BooleanSetting extends Setting {

    private boolean value;

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }
    
    public BooleanSetting(String name, boolean value, ParentSetting parent) {
        super(name, parent);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        jsonObject.addProperty(getName(), getValue());
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        setValue(jsonObject.get(getName()).getAsBoolean());
    }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [Boolean]: " + getValue() + "\n");
    }
}
