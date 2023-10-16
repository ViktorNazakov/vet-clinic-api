package com.uni.vetclinicapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Delivers a singleton instance of the ModelMapper class.
 */
@Configuration
public class ModelMapperBean {


    /**
     * Delivers the only instance of ModelMapper type in the application context.
     *
     * @return - the instance of type ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
