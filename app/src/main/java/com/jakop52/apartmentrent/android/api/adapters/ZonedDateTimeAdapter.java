package com.jakop52.apartmentrent.android.api.adapters;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ZonedDateTime.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
