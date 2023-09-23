package br.com.fiap.findwanted.service;

public interface DataConverter {

    <T> T getData(String json, Class<T> tClass);
}
