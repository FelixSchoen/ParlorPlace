package com.fschoen.parlorplace.backend.configuration;

import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {MVCConfig.class})
public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }

}
