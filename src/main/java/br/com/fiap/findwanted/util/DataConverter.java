package br.com.fiap.findwanted.util;

public interface DataConverter {

    <T> T getData(String json, Class<T> tClass);
}
