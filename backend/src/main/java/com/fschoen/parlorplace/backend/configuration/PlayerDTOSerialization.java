package com.fschoen.parlorplace.backend.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;

import java.io.IOException;
import java.io.StringWriter;

public class PlayerDTOSerialization {

    public static class PlayerDTOKeySerializer extends JsonSerializer<PlayerDTO<?>> {

        private static final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void serialize(PlayerDTO<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, value);
            gen.writeFieldName(stringWriter.toString());
        }

    }

    public static class PlayerDTOKeyDeserializer extends KeyDeserializer {

        @Override
        public WerewolfPlayerDTO deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(key, WerewolfPlayerDTO.class);
        }

    }

}
