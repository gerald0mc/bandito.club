package club.bandito.client.module.setting;

import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedWriter;
import java.io.IOException;

public class PositionSetting extends Setting {

    private BlockPos position;

    public PositionSetting(String name, BlockPos pos) {
        super(name);
        this.position = pos;
    }
    
    public PositionSetting(String name, BlockPos pos, ParentSetting parent) {
        super(name, parent);
        this.position = pos;
    }

    public PositionSetting(String name) {
        this(name, null);
    }

    public BlockPos getPosition() {
        return position;
    }

    public void setPosition(BlockPos position) {
        this.position = position;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        final JsonObject posSettingObj = new JsonObject();

        if (getPosition() != null) {
            posSettingObj.addProperty("X", getPosition().getX());
            posSettingObj.addProperty("Y", getPosition().getY());
            posSettingObj.addProperty("Z", getPosition().getZ());
        } else {
            posSettingObj.addProperty("NotSet", true);
        }
        jsonObject.add(getName(), posSettingObj);
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        final JsonObject posObj = jsonObject.get(getName()).getAsJsonObject();
        if (!posObj.has("NotSet")) {
            final int x = posObj.get("X").getAsInt();
            final int y = posObj.get("Y").getAsInt();
            final int z = posObj.get("Z").getAsInt();
            setPosition(new BlockPos(x, y, z));
        }
    }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [Position]\n");
    }
}
