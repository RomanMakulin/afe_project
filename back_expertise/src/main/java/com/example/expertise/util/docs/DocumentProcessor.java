package com.example.expertise.util.docs;

import com.example.expertise.model.expertise.ExpertiseQuestion;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Класс для обработки документов в формате DOCX.
 */
@Component
public class DocumentProcessor {

    private static final Logger log = LoggerFactory.getLogger(DocumentProcessor.class);

    private final DocumentPhotoInserter documentPhotoInserter;
    private final DocumentChecklistInserter documentChecklistInserter;

    public static final String QUESTION_SEPARATOR = "<!-- QUESTION_SEPARATOR -->";
    public static final String LINE_SEPARATOR = "<!-- LINE_SEPARATOR -->";
    private static final int PARAGRAPH_SPACING_TWIPS = 240;

    public DocumentProcessor(@Lazy DocumentPhotoInserter documentPhotoInserter,
                             DocumentChecklistInserter documentChecklistInserter) {
        this.documentPhotoInserter = documentPhotoInserter;
        this.documentChecklistInserter = documentChecklistInserter;
    }

    /**
     * Процесс проставления значений в документе по закладкам
     *
     * @param template  - шаблонный документ
     * @param mergeData - данные для заполнения
     * @param photoMap  - карта изображений
     * @param questions - список вопросов экспертизы
     * @return - обработанный документ в виде массива байтов
     */
    public byte[] processDocument(WordprocessingMLPackage template,
                                  Map<DataFieldName, String> mergeData,
                                  Map<String, byte[]> photoMap,
                                  List<byte[]> photoDocs,
                                  List<ExpertiseQuestion> questions,
                                  MultipartFile screenMap) {
        try {
            MailMerger.setMERGEFIELDInOutput(MailMerger.OutputField.REMOVED);
            MailMerger.performMerge(template, mergeData, true);

            splitParagraphs(template);

            documentChecklistInserter.insertAnswerChecklist(template, questions); // проставляем чек-листы по заданным ключам (по ответам на вопросы)
            documentPhotoInserter.insertAnswerImages(template, photoMap); // вставляем изображения по заданным ключам (по ответам на вопросы)
            documentPhotoInserter.insertPhotoDocs(template, photoDocs); // вставляем фото документов эксперта в приложении экспертизы
            documentPhotoInserter.insertScreenMap(template, screenMap); // вставляем карту (скрин) объекта экспертизы

            // Сохранение итогового файла
            File outputFile = new File("output.docx");
            template.save(outputFile);
            log.info("Итоговый файл сохранён: {}", outputFile.getAbsolutePath());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            template.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Ошибка при обработке документа", e);
            throw new RuntimeException("Не удалось обработать DOCX-документ", e);
        }
    }

    /**
     * Разбивает на параграфы по заданным разделит  елям
     *
     * @param wordPackage - обрабатываемый документ
     */
    private void splitParagraphs(WordprocessingMLPackage wordPackage) {
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        List<Object> content = mainDocumentPart.getContent();

        for (int i = 0; i < content.size(); i++) {
            if (content.get(i) instanceof P) {
                P paragraph = (P) content.get(i);
                String text = getParagraphText(paragraph);
                if (text.contains(QUESTION_SEPARATOR) || text.contains(LINE_SEPARATOR)) {
                    i = replaceParagraphWithSplitContent(content, i, text);
                }
            }
        }
    }

    /**
     * Метод для замены параграфа на разделённый контент
     *
     * @param content - список объектов, содержащий параграфы
     * @param index   - текущий индекс параграфа
     * @param text    - текст параграфа
     * @return новый индекс параграфа после замены
     */
    private int replaceParagraphWithSplitContent(List<Object> content, int index, String text) {
        // Сначала разбиваем по QUESTION_SEPARATOR
        String[] questionParts = text.split(QUESTION_SEPARATOR);
        content.remove(index);

        int insertIndex = index;
        for (String questionPart : questionParts) {
            if (!questionPart.trim().isEmpty()) {
                // Затем разбиваем каждую часть по LINE_SEPARATOR
                insertIndex = addLinesToContent(content, insertIndex, questionPart);
            }
        }
        return insertIndex - 1;
    }

    /**
     * Метод для добавления строк в список объектов параграфов
     *
     * @param content      - список объектов, содержащий параграфы
     * @param insertIndex  - индекс вставки строки
     * @param questionPart - строка, которая будет добавлена в список объектов параграфов
     * @return новый индекс параграфа после добавления строки
     */
    private int addLinesToContent(List<Object> content, int insertIndex, String questionPart) {
        String[] lines = questionPart.split(LINE_SEPARATOR);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                content.add(insertIndex++, createFormattedParagraph(line));
            }
        }
        return insertIndex;
    }

    /**
     * Создание параграфа с заданным текстом и форматированием
     *
     * @param text - текст параграфа
     * @return - созданный параграф
     */
    private P createFormattedParagraph(String text) {
        P paragraph = new P();
        R run = new R();
        Text runText = new Text();
        runText.setValue(text.trim());
        run.getContent().add(runText);

        if (text.trim().startsWith("Вопрос №")) {
            RPr runProperties = new RPr();
            BooleanDefaultTrue bold = new BooleanDefaultTrue();
            bold.setVal(true);
            runProperties.setB(bold);
            run.setRPr(runProperties);
        }

        PPr paragraphProperties = new PPr();
        PPrBase.Spacing spacing = new PPrBase.Spacing();
        spacing.setAfter(BigInteger.valueOf(PARAGRAPH_SPACING_TWIPS));
        paragraphProperties.setSpacing(spacing);
        paragraph.setPPr(paragraphProperties);

        paragraph.getContent().add(run);
        return paragraph;
    }

    /**
     * Получает текст параграфа
     *
     * @param paragraph - параграф, из которого нужно получить текст
     * @return - текст параграфа
     */
    private String getParagraphText(P paragraph) {
        StringBuilder text = new StringBuilder();
        for (Object content : paragraph.getContent()) {
            if (content instanceof R) {
                R run = (R) content;
                for (Object runContent : run.getContent()) {
                    if (runContent instanceof Text) {
                        text.append(((Text) runContent).getValue());
                    }
                }
            }
        }
        return text.toString();
    }
}