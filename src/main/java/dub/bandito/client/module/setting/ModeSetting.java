package club.bandito.client.module.setting;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

public class ModeSetting extends Setting {

    private final String[] modes;
    private int modeIndex;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name);
        this.modes = modes;
        this.modeIndex = indexOf(defaultMode);
    }
    
    public ModeSetting(String name, String defaultMode, ParentSetting parent, String... modes) {
        super(name, parent);
        this.modes = modes;
        this.modeIndex = indexOf(defaultMode);
    }

    public int indexOf(String mode) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(mode))
                return i;
        }

        return -1;
    }

    public String getMode() {
        return modes[modeIndex];
    }

    public String increment() {
        if (modeIndex == modes.length - 1) {
            modeIndex = 0;
        } else {
            modeIndex++;
        }

        return getMode();
    }

    public String decrement() {
        if (modeIndex == 0) {
            modeIndex = modes.length - 1;
        } else {
            modeIndex--;
        }

        return getMode();
    }

    public void setModeIndex(int index) {
        this.modeIndex = index;
    }

    public String[] getModes() {
        return modes;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        jsonObject.addProperty(getName(), getMode());
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        setModeIndex(indexOf(jsonObject.get(getName()).getAsString()));
    }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [Mode]: " + Arrays.asList(getModes()) + "\n");
    }
}
