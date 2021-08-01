package com.fschoen.parlorplace.backend.configuration;

import com.fasterxml.jackson.databind.*;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.*;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToEnable(SerializationFeature.WRAP_ROOT_VALUE);
        builder.featuresToEnable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        return builder;
    }

}
