package br.com.fezor.screenmatch.services;

public interface IDataConvert {
    <T> T getData(String json, Class<T> clazz);
}
