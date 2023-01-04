package club.bandito.client.module.setting;

import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ListSetting extends Setting {
    private final Setting[] settingList;

    public ListSetting(String name, Setting[] list) {
        super(name);
        this.settingList = list;
    }
    
    public ListSetting(String name, Setting[] list, ParentSetting parent) {
        super(name, parent);
        this.settingList = list;
    }

    public List<Setting> getList() {
        return Arrays.asList(settingList);
    }

    public Setting getSettingIndex(int index) {
        return settingList[index];
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        final JsonObject listSettingObj = new JsonObject();
        for (Setting s : getList()) {
            s.saveConfig(listSettingObj);
        }
        jsonObject.add(getName(), listSettingObj);
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        final JsonObject listObj = jsonObject.get(getName()).getAsJsonObject();
        for (Setting s : getList()) {
            if (!listObj.has(s.getName())) continue;
            s.loadConfig(listObj);
        }
    }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [List] {\n");
        for (Setting s : getList()) {
            s.saveShowoff(bufferedWriter);
        }
        bufferedWriter.write("}\n");
    }
}
