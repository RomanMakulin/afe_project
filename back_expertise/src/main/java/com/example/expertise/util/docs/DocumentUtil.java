package com.example.expertise.util.docs;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.wml.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Утилитный класс для работы с документами Word (параграфы, текст, форматирование).
 */
@Slf4j
@Component
public class DocumentUtil {

    private static final int PARAGRAPH_SPACING_TWIPS = 240;
    private final ObjectFactory factory;

    public DocumentUtil() {
        this.factory = new ObjectFactory();
    }

    /**
     * Получает текст параграфа.
     *
     * @param paragraph Параграф, из которого нужно извлечь текст
     * @return Текст параграфа
     */
    public String getParagraphText(P paragraph) {
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

    /**
     * Создает параграф с заданным текстом и форматированием.
     *
     * @param text Текст параграфа
     * @return Созданный параграф
     */
    public P createFormattedParagraph(String text) {
        P paragraph = factory.createP();
        R run = factory.createR();
        Text runText = factory.createText();
        runText.setValue(text.trim());
        run.getContent().add(runText);

        if (text.trim().startsWith("Вопрос №")) {
            RPr runProperties = factory.createRPr();
            BooleanDefaultTrue bold = factory.createBooleanDefaultTrue();
            bold.setVal(true);
            runProperties.setB(bold);
            run.setRPr(runProperties);
        }

        PPr paragraphProperties = factory.createPPr();
        PPrBase.Spacing spacing = factory.createPPrBaseSpacing();
        spacing.setAfter(BigInteger.valueOf(PARAGRAPH_SPACING_TWIPS));
        paragraphProperties.setSpacing(spacing);
        paragraph.setPPr(paragraphProperties);

        paragraph.getContent().add(run);
        return paragraph;
    }

    /**
     * Разбивает текст параграфа на части по разделителю и заменяет параграф на новые.
     *
     * @param content    Список объектов документа
     * @param index      Индекс текущего параграфа
     * @param text       Текст параграфа
     * @param separator  Разделитель для разбиения текста
     * @param lineSeparator Разделитель для разбиения строк внутри частей
     * @return Новый индекс после вставки
     */
    public int replaceParagraphWithSplitContent(List<Object> content, int index, String text, String separator, String lineSeparator) {
        String[] parts = text.split(separator);
        content.remove(index);

        int insertIndex = index;
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                insertIndex = addLinesToContent(content, insertIndex, part, lineSeparator);
            }
        }
        return insertIndex - 1;
    }

    /**
     * Добавляет строки в список объектов документа.
     *
     * @param content     Список объектов документа
     * @param insertIndex Индекс для вставки
     * @param part        Часть текста для добавления
     * @param lineSeparator Разделитель строк
     * @return Новый индекс после вставки
     */
    private int addLinesToContent(List<Object> content, int insertIndex, String part, String lineSeparator) {
        String[] lines = part.split(lineSeparator);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                content.add(insertIndex++, createFormattedParagraph(line));
            }
        }
        return insertIndex;
    }

    /**
     * Получает все элементы заданного типа в документе.
     *
     * @param obj      Объект, в котором нужно найти элементы
     * @param toSearch Тип элементов для поиска
     * @return Список найденных элементов
     */
    public List<Object> getAllElements(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        if (obj instanceof jakarta.xml.bind.JAXBElement) {
            obj = ((jakarta.xml.bind.JAXBElement<?>) obj).getValue();
        }
        if (toSearch.isInstance(obj)) {
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            for (Object child : ((ContentAccessor) obj).getContent()) {
                result.addAll(getAllElements(child, toSearch));
            }
        }
        return result;
    }

    /**
     * Находит родительский параграф для текста.
     *
     * @param text Текст
     * @return Родительский параграф или null
     */
    public P findParentParagraph(Text text) {
        return (P) getParentOfType(text, P.class);
    }

    /**
     * Получает родительский объект заданного типа.
     *
     * @param obj  Объект, для которого нужно найти родителя
     * @param type Тип родительского объекта
     * @return Родительский объект или null
     */
    public Object getParentOfType(Object obj, Class<?> type) {
        Object current = obj;
        while (current != null) {
            if (type.isAssignableFrom(current.getClass())) {
                return current;
            }
            if (current instanceof org.jvnet.jaxb2_commons.ppp.Child) {
                current = ((org.jvnet.jaxb2_commons.ppp.Child) current).getParent();
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Проверяет, содержит ли текст плейсхолдер из photoMap.
     *
     * @param textValue Текст для проверки
     * @param photoMap  Карта изображений
     * @return true, если текст содержит плейсхолдер
     */
    public boolean containsPlaceholder(String textValue, Map<String, byte[]> photoMap) {
        return photoMap.keySet().stream().anyMatch(textValue::contains);
    }

    /**
     * Находит запись в photoMap, соответствующую плейсхолдеру в тексте.
     *
     * @param textValue Текст для проверки
     * @param photoMap  Карта изображений
     * @return Optional с найденной записью
     */
    public Optional<Map.Entry<String, byte[]>> findPlaceholderEntry(String textValue, Map<String, byte[]> photoMap) {
        return findPlaceholderEntry(textValue, photoMap, null);
    }

    /**
     * Находит запись в photoMap, соответствующую плейсхолдеру в тексте, исключая указанный плейсхолдер.
     *
     * @param textValue         Текст для проверки
     * @param photoMap          Карта изображений
     * @param excludePlaceholder Плейсхолдер для исключения
     * @return Optional с найденной записью
     */
    public Optional<Map.Entry<String, byte[]>> findPlaceholderEntry(String textValue, Map<String, byte[]> photoMap, String excludePlaceholder) {
        return photoMap.entrySet().stream()
                .filter(entry -> (excludePlaceholder == null || !entry.getKey().equals(excludePlaceholder))
                        && textValue.contains(entry.getKey()))
                .findFirst();
    }
}