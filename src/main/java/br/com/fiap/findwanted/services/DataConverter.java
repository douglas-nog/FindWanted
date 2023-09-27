package br.com.fiap.findwanted.services;

public interface DataConverter {

    <T> T getData(String json, Class<T> tClass);
}
