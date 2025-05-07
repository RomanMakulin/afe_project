package com.example.expertise.services.gigachat;

/**
 * Интерфейс для авторизации с API GigaChat
 */
public interface GigaChatAuthService {

    /**
     * Получить токен авторизации для API GigaChat
     *
     * @return токен авторизации
     */
    String getAuthToken();

}
