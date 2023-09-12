package com.jakop52.apartmentrent.android.api.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class ZonedDateTimeSerializer implements JsonSerializer<ZonedDateTime> {

    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}