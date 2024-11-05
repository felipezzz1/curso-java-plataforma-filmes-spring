package br.com.fezor.screenmatchSpring.services;

public interface IDataConvert {
    <T> T getData(String json, Class<T> clazz);
}
