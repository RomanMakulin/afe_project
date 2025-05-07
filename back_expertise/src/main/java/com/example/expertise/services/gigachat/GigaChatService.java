package com.example.expertise.services.gigachat;

/**
 * Интерфейс для работы с API GigaChat
 */
public interface GigaChatService {

    /**
     * Отправить сообщение в чат (ИИ)
     *
     * @param message текст сообщения
     * @return сгенерированный ответ сервера
     */
    String sendMessage(String message);

}
