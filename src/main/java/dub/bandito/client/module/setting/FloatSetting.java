package club.bandito.client.module.setting;

import com.google.gson.JsonObject;
import net.minecraft.util.math.MathHelper;

import java.io.BufferedWriter;
import java.io.IOException;

public class FloatSetting extends Setting {

    private final float min;
    private final float max;
    private float value;

    public FloatSetting(String name, float value, float min, float max) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }
    
    public FloatSetting(String name, float value, float min, float max, ParentSetting parent) {
        super(name, parent);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = MathHelper.clamp(value, min, max);
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public void saveConfig(JsonObject jsonObject) {
        jsonObject.addProperty(getName(), getValue());
    }

    @Override
    public void loadConfig(JsonObject jsonObject) {
        setValue(jsonObject.get(getName()).getAsFloat());
    }

    @Override
    public void saveShowoff(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(getName() + " [Number]: Min (" + getMin() + ") Max (" + getMax() + ")\n");
    }
}
