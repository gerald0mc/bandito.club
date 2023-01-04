package club.bandito.client.module.setting;

import com.google.gson.JsonObject;
import org.lwjgl.input.Keyboard;

import java.io.BufferedWriter;
import java.io.IOException;

public class BindSetting extends Setting{
    private int bind;

    public BindSetting(int bind) {
        super("Bind");
        this.bind = bind;
    }

    public BindSetting(int bind, ParentSetting parent) {
        super("Bind", parent);
        this.bind = bind;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) { }

    @Override
    public void loadConfig(JsonObject jsonObject) { }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {

    }
}
