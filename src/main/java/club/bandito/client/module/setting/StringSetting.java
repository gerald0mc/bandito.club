package club.bandito.client.module.setting;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;

public class StringSetting extends Setting {
    private String value;

    public StringSetting(String name, String value) {
        super(name);
        this.value = value;
    }
    
    public StringSetting(String name, String value, ParentSetting parent) {
        super(name, parent);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        jsonObject.addProperty(getName(), getValue());
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        setValue(jsonObject.get(getName()).getAsString());
    }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [String]: " + getValue() + "\n");
    }
}
