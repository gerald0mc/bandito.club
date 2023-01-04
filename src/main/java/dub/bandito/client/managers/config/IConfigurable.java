package club.bandito.client.managers.config;

import com.google.gson.JsonObject;

public interface IConfigurable {

    String getName();

    void readFromJson(JsonObject json);

    void writeToJson(JsonObject json);
}
