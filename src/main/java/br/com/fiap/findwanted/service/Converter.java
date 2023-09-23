package br.com.fiap.findwanted.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Converter implements DataConverter {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Override
    public <T> T getData(String json, Class<T> tClass) {
        try {
            return mapper().readValue(json, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
