package com.example.progettoprova.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
//    per utilizzare nomi dei campi per mappare -> .setFieldMatchingEnabled(true)
//    accedo anche sui campi privati -> .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }
}
