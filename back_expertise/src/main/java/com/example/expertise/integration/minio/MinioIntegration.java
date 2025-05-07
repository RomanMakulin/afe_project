package com.example.expertise.integration.minio;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Интерфейс интеграции с MinIO
 */
public interface MinioIntegration {

    /**
     * Получить экспертизу пользователя из MinIO (шаблон)
     *
     * @param templateName название шаблона экспертизы
     * @return файл экспертизы
     */
    InputStreamResource getExpertiseFile(String templateName) throws IOException;

    /**
     * Получить фотографию ответа экспертизы из MinIO (шаблон)
     *
     * @param photoPath путь к фотографии
     * @return фотография ответа экспертизы
     */
    byte[] getExpertisePhotoAsBytes(String photoPath);

    /**
     * Получить файл пользователя из MinIO
     *
     * @param fileName      название файла
     * @param fileExtension расширение файла
     * @param fileBucket    название бакета
     * @return файл пользователя в виде байтов
     */
    byte[] getProfileFileAsBytes(@NotNull(message = "fileName null") String fileName,
                                 @NotNull(message = "fileExtension null") String fileExtension,
                                 @NotNull(message = "fileBucket null") String fileBucket);

    /**
     * Загрузить фотографию ответа в MinIO
     *
     * @param photoName название фотографии
     * @param photo     фото
     */
    void uploadAnswerPhoto(String photoName, MultipartFile photo);

    /**
     * Удалить все фотографии ответов экспертизы по id экспертизы
     *
     * @param expertiseId id экспертизы
     */
    void deleteAllAnswerPhotos(UUID expertiseId);

    /**
     * Удалить фотографию ответа экспертизы по пути к файлу
     *
     * @param filePath путь к файлу в хранилище
     */
    void deleteAnswerPhoto(String filePath);

}
