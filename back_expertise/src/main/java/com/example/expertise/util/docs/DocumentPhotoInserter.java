package com.example.expertise.util.docs;

import com.example.expertise.enums.Bookmarks;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Класс для вставки фотографий в документ Word.
 */
@Component
public class DocumentPhotoInserter {

    private static final Logger log = LoggerFactory.getLogger(DocumentPhotoInserter.class);

    private final FormatConverter formatConverter;
    private final TableUtil tableUtil;
    private final DocumentUtil documentUtil;

    public DocumentPhotoInserter(FormatConverter formatConverter) {
        this.formatConverter = formatConverter;
        this.tableUtil = new TableUtil();
        this.documentUtil = new DocumentUtil();
    }

    /**
     * Вставляет скриншот карты экспертизы в документ Word по плейсхолдеру expertiseMapScreenshot.
     *
     * @param photoScreenMap Скриншот карты (MultipartFile)
     * @param wordPackage    Документ Word (WordprocessingMLPackage)
     * @throws Exception Если произошла ошибка при обработке изображения или документа
     */
    protected void insertScreenMap(WordprocessingMLPackage wordPackage, MultipartFile photoScreenMap) {
        try {
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            List<Object> textElements = documentUtil.getAllElements(mainDocumentPart, Text.class);

            for (Object obj : textElements) {
                Text text = (Text) obj;
                String textValue = text.getValue();

                if (textValue == null || !textValue.equals("expertiseMapScreenshot")) {
                    continue;
                }

                // Получаем родительский параграф
                P parentParagraph = documentUtil.findParentParagraph(text);
                if (parentParagraph == null) {
                    log.warn("Не удалось найти родительский параграф для плейсхолдера expertiseMapScreenshot");
                    continue;
                }

                // Очищаем текст плейсхолдера
                text.setValue("");

                // Получаем байты изображения
                byte[] imageData = photoScreenMap.getBytes();
                if (imageData.length == 0) {
                    log.warn("Файл изображения expertiseMapScreenshot пустой");
                    continue;
                }

                // Создаём Run с изображением
                R imageRun = tableUtil.createImageRun(
                        wordPackage,
                        imageData,
                        "Скриншот карты экспертизы",
                        TableUtil.DEFAULT_SCREEN_MAP_WIDTH
                );

                // Добавляем изображение в параграф
                parentParagraph.getContent().add(imageRun);

                log.info("Скриншот карты экспертизы успешно вставлен");
                break;
            }
        } catch (Exception e) {
            log.error("Ошибка при вставке скриншота карты экспертизы: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Вставляет изображения документов (например, дипломов) в документ в раздел "Приложения".
     */
    protected void insertPhotoDocs(WordprocessingMLPackage wordPackage, List<byte[]> photoDocs) {
        try {
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            List<Object> textElements = documentUtil.getAllElements(mainDocumentPart, Text.class);

            for (Object obj : textElements) {
                Text text = (Text) obj;
                if (!isPhotoDocsBookmark(text)) {
                    continue;
                }

                replaceBookmarkText(text, Bookmarks.PHOTO_DOCS.getName());
                P parentParagraph = getParentParagraph(text, Bookmarks.PHOTO_DOCS.getName());
                if (parentParagraph == null) continue;

                R parentRun = (R) text.getParent();
                int runIndex = parentParagraph.getContent().indexOf(parentRun);
                if (runIndex == -1) {
                    log.warn("Не удалось найти run в параграфе для закладки {}", Bookmarks.PHOTO_DOCS.getName());
                    continue;
                }

                int docIndex = 1;
                for (byte[] docData : photoDocs) {
                    List<byte[]> imageDataList = formatConverter.convertPdfToImages(docData);
                    if (imageDataList.isEmpty()) {
                        log.warn("Не удалось преобразовать PDF в изображение для документа #{}, размер данных: {}", docIndex, docData.length);
                        docIndex++;
                        continue;
                    }

                    for (int page = 0; page < imageDataList.size(); page++) {
                        byte[] imageData = imageDataList.get(page);
                        R imageRun = tableUtil.createImageRun(
                                wordPackage,
                                imageData,
                                "Документ " + docIndex + ", страница " + (page + 1),
                                TableUtil.DEFAULT_IMAGE_WIDTH_TWIPS
                        );
                        parentParagraph.getContent().add(runIndex + 1 + page, imageRun);
                    }
                    runIndex += imageDataList.size();
                    docIndex++;
                }
                break; // Закладка встречается только раз
            }
        } catch (Exception e) {
            log.error("Ошибка при вставке изображений документов: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Вставляет изображения по плейсхолдерам в таблицу с двумя колонками.
     */
    protected void insertAnswerImages(WordprocessingMLPackage wordPackage, Map<String, byte[]> photoMap) {
        try {
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            List<Object> textElements = documentUtil.getAllElements(mainDocumentPart, Text.class);

            List<Replacement> replacements = new ArrayList<>();
            int imageCounter = 1;

            for (int i = 0; i < textElements.size(); i++) {
                Text text = (Text) textElements.get(i);
                if (!hasPlaceholder(text, photoMap)) {
                    continue;
                }

                P parentParagraph = getParentParagraph(text, text.getValue());
                if (parentParagraph == null) {
                    continue;
                }

                int paragraphIndex = mainDocumentPart.getContent().indexOf(parentParagraph);
                if (paragraphIndex == -1) {
                    log.warn("Не удалось найти индекс параграфа для текста: {}", text.getValue());
                    continue;
                }

                // Создаём пустой параграф (пустая строка)
                P spaceParagraph = documentUtil.createFormattedParagraph("");

                // Создаём таблицу
                Tbl table = tableUtil.createTable(
                        TableUtil.DEFAULT_TABLE_WIDTH_TWIPS,
                        TableUtil.DEFAULT_WIDTH_TYPE,
                        TableUtil.DEFAULT_BORDER_SIZE,
                        TableUtil.DEFAULT_BORDER_COLOR,
                        TableUtil.DEFAULT_BORDER_STYLE
                );
                List<P> paragraphsToRemove = new ArrayList<>();
                paragraphsToRemove.add(parentParagraph);

                while (i < textElements.size()) {
                    Text currentText = (Text) textElements.get(i);
                    Optional<Map.Entry<String, byte[]>> currentEntryOpt = processPlaceholder(currentText, photoMap);
                    if (currentEntryOpt.isEmpty()) {
                        break;
                    }

                    Map.Entry<String, byte[]> currentEntry = currentEntryOpt.get();
                    Tr tableRow = tableUtil.createTableRow();
                    table.getContent().add(tableRow);

                    // Первая ячейка с текущим изображением
                    Tc tableCell1 = tableUtil.createImageCell(
                            wordPackage,
                            currentEntry.getValue(),
                            TableUtil.DEFAULT_IMAGE_WIDTH_TWIPS,
                            "Рисунок " + imageCounter++
                    );
                    tableRow.getContent().add(tableCell1);

                    // Вторая ячейка - либо следующее изображение, либо пустая
                    Tc tableCell2 = tableUtil.createEmptyCell();
                    if (i + 1 < textElements.size()) {
                        Text nextText = (Text) textElements.get(i + 1);
                        Optional<Map.Entry<String, byte[]>> nextEntryOpt = processPlaceholder(nextText, photoMap);
                        if (nextEntryOpt.isPresent()) {
                            tableCell2 = tableUtil.createImageCell(
                                    wordPackage,
                                    nextEntryOpt.get().getValue(),
                                    TableUtil.DEFAULT_IMAGE_WIDTH_TWIPS,
                                    "Рисунок " + imageCounter++
                            );
                            P nextParagraph = documentUtil.findParentParagraph(nextText);
                            if (nextParagraph != null) {
                                paragraphsToRemove.add(nextParagraph);
                            }
                            i++; // Пропускаем следующий элемент, так как мы его уже обработали
                        }
                    }
                    tableRow.getContent().add(tableCell2);
                    i++; // Переходим к следующему элементу
                }

                // Добавляем замены
                replacements.add(new Replacement(paragraphIndex, spaceParagraph));
                replacements.add(new Replacement(paragraphIndex + 1, table));

                // Удаляем параграфы
                for (P paragraph : paragraphsToRemove) {
                    mainDocumentPart.getContent().remove(paragraph);
                }
            }

            // Применяем замены
            replacements.sort((r1, r2) -> Integer.compare(r2.index, r1.index));
            for (Replacement r : replacements) {
                mainDocumentPart.getContent().add(r.index, r.content);
            }

            log.info("Вставлено {} изображений", imageCounter - 1);
        } catch (Exception e) {
            log.error("Ошибка при вставке изображений (ответы на вопросы): {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static class Replacement {
        int index;
        Object content;

        Replacement(int index, Object content) {
            this.index = index;
            this.content = content;
        }
    }

    // Вспомогательные методы

    private boolean isPhotoDocsBookmark(Text text) {
        String textValue = text.getValue();
        return textValue != null && textValue.contains(Bookmarks.PHOTO_DOCS.getName());
    }

    private boolean hasPlaceholder(Text text, Map<String, byte[]> photoMap) {
        String textValue = text.getValue();
        return textValue != null && documentUtil.containsPlaceholder(textValue, photoMap);
    }

    private void replaceBookmarkText(Text text, String bookmark) {
        text.setValue(text.getValue().replace(bookmark, ""));
    }

    private P getParentParagraph(Text text, String logContext) {
        P parentParagraph = documentUtil.findParentParagraph(text);
        if (parentParagraph == null) {
            log.warn("Не удалось найти родительский параграф для текста: {}", logContext);
        }
        return parentParagraph;
    }

    private Optional<Map.Entry<String, byte[]>> processPlaceholder(Text text, Map<String, byte[]> photoMap) {
        String textValue = text.getValue();
        if (textValue == null || !documentUtil.containsPlaceholder(textValue, photoMap)) {
            return Optional.empty();
        }

        Optional<Map.Entry<String, byte[]>> entryOpt = documentUtil.findPlaceholderEntry(textValue, photoMap);
        if (entryOpt.isPresent()) {
            Map.Entry<String, byte[]> entry = entryOpt.get();
            log.info("Найден плейсхолдер '{}'", entry.getKey());
            text.setValue(textValue.replace(entry.getKey(), ""));
        }
        return entryOpt;
    }

    private Tc processNextPlaceholder(WordprocessingMLPackage wordPackage, List<Object> textElements, int index,
                                      Map<String, byte[]> photoMap, String currentKey, List<P> paragraphsToRemove, int imageCounter) throws Exception {
        if (index >= textElements.size()) {
            return tableUtil.createEmptyCell();
        }

        Text nextText = (Text) textElements.get(index);
        String nextTextValue = nextText.getValue();
        if (nextTextValue == null || !documentUtil.containsPlaceholder(nextTextValue, photoMap)) {
            return tableUtil.createEmptyCell();
        }

        Optional<Map.Entry<String, byte[]>> nextEntryOpt = documentUtil.findPlaceholderEntry(nextTextValue, photoMap, currentKey);
        if (nextEntryOpt.isEmpty()) {
            return tableUtil.createEmptyCell();
        }

        Map.Entry<String, byte[]> nextEntry = nextEntryOpt.get();
        log.info("Найден соседний плейсхолдер '{}'", nextEntry.getKey());
        nextText.setValue(nextTextValue.replace(nextEntry.getKey(), ""));

        Tc tableCell = tableUtil.createImageCell(
                wordPackage,
                nextEntry.getValue(),
                TableUtil.DEFAULT_IMAGE_WIDTH_TWIPS,
                "Рисунок " + imageCounter
        );
        P nextParagraph = documentUtil.findParentParagraph(nextText);
        if (nextParagraph != null) {
            paragraphsToRemove.add(nextParagraph);
        }
        textElements.set(index, nextText); // Обновляем элемент в списке
        return tableCell;
    }

    private void removeParagraphs(MainDocumentPart mainDocumentPart, List<P> paragraphsToRemove, int paragraphIndex) {
        for (P paragraph : paragraphsToRemove) {
            int index = mainDocumentPart.getContent().indexOf(paragraph);
            if (index != -1) {
                mainDocumentPart.getContent().remove(index);
                if (index <= paragraphIndex) {
                    paragraphIndex--;
                }
            }
        }
    }
}