package com.example.expertise.util.docs;

import com.example.expertise.model.checklist.Checklist;
import com.example.expertise.model.checklist.ChecklistInstance;
import com.example.expertise.model.checklist.ChecklistInstanceField;
import com.example.expertise.model.checklist.SpecValue;
import com.example.expertise.model.expertise.ExpertiseQuestion;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для вставки ответов на вопросы в документ Word.
 */
@Component
public class DocumentChecklistInserter {

    private static final Logger log = LoggerFactory.getLogger(DocumentChecklistInserter.class);

    private final TableUtil tableUtil;
    private final DocumentUtil documentUtil;

    public DocumentChecklistInserter(TableUtil tableUtil, DocumentUtil documentUtil) {
        this.tableUtil = tableUtil;
        this.documentUtil = documentUtil;
    }

    public void insertAnswerChecklist(WordprocessingMLPackage wordPackage,
                                      List<ExpertiseQuestion> questions) throws Exception {
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        Map<String, List<ChecklistInstance>> checklistMap = buildChecklistMap(questions);
        List<Object> content = mainDocumentPart.getContent();

        for (int i = 0; i < content.size(); i++) {
            Object element = content.get(i);
            if (!(element instanceof P)) {
                continue;
            }

            P paragraph = (P) element;
            List<Object> textElements = documentUtil.getAllElements(paragraph, Text.class);
            if (textElements.isEmpty()) {
                continue;
            }

            StringBuilder paragraphText = new StringBuilder();
            for (Object textObj : textElements) {
                Text text = (Text) textObj;
                paragraphText.append(text.getValue());
            }
            String fullText = paragraphText.toString();

            if (!checklistMap.keySet().stream().anyMatch(fullText::contains)) {
                continue;
            }

            content.remove(i);

            for (String placeholder : checklistMap.keySet()) {
                if (fullText.contains(placeholder)) {
                    List<ChecklistInstance> instances = checklistMap.get(placeholder);
                    P spaceParagraph = documentUtil.createFormattedParagraph("");

                    // Добавляем типовой текст перед таблицей
                    P introParagraph = createIntroParagraph(instances);
                    Tbl checklistTable = createChecklistTable(instances);

                    content.add(i, spaceParagraph);
                    content.add(i + 1, introParagraph);
                    content.add(i + 2, checklistTable);
                    i += 3; // Сдвигаем индекс на 3 (пустой параграф, вводный текст, таблица)
                }
            }

            i--; // Корректируем индекс после обработки
        }
    }

    private Map<String, List<ChecklistInstance>> buildChecklistMap(List<ExpertiseQuestion> questions) {
        Map<String, List<ChecklistInstance>> checklistMap = new HashMap<>();
        Map<UUID, List<ChecklistInstance>> groupedByChecklist = questions.stream()
                .filter(q -> q.getChecklistInstances() != null)
                .flatMap(q -> q.getChecklistInstances().stream())
                .collect(Collectors.groupingBy(ci -> ci.getChecklist().getId()));

        groupedByChecklist.forEach((checklistId, instances) -> {
            String placeholder = String.format("[CHECKLIST_%s]", checklistId);
            checklistMap.put(placeholder, instances);
        });

        return checklistMap;
    }

    /**
     * Создает вводный параграф с типовым текстом перед таблицей.
     *
     * @param instances список экземпляров чек-листа
     * @return параграф с типовым текстом
     */
    private P createIntroParagraph(List<ChecklistInstance> instances) {
        ChecklistInstance firstInstance = instances.getFirst();
        Checklist checklist = firstInstance.getChecklist();

        // Формируем текст с динамическими полями из checklist_instance_fields
        StringBuilder fieldsText = new StringBuilder();
        if (firstInstance.getChecklistInstanceFields() != null && !firstInstance.getChecklistInstanceFields().isEmpty()) {
            for (ChecklistInstanceField field : firstInstance.getChecklistInstanceFields()) {
                String fieldName = field.getChecklistField().getFieldName();
                String fieldValue = field.getValue() != null ? field.getValue() : "не указано";
                fieldsText.append(fieldName).append(": ").append(fieldValue).append("; ");
            }
            // Удаляем последнюю точку с запятой и пробел
            if (fieldsText.length() > 2) {
                fieldsText.setLength(fieldsText.length() - 2);
            }
        } else {
            fieldsText.append("данные не указаны");
        }

        // Заменяем placeholder в шаблоне описания чек-листа
        String introText = checklist.getDescription();
        introText = introText.replace("{location}", fieldsText.toString());
        return documentUtil.createFormattedParagraph(introText);
    }

    private Tbl createChecklistTable(List<ChecklistInstance> instances) {
        Tbl table = tableUtil.createTable(
                TableUtil.DEFAULT_TABLE_WIDTH_TWIPS,
                TableUtil.DEFAULT_WIDTH_TYPE,
                TableUtil.DEFAULT_BORDER_SIZE,
                TableUtil.DEFAULT_BORDER_COLOR,
                TableUtil.DEFAULT_BORDER_STYLE
        );

        String checklistTitle = String.format("Техническое описание самостроя постройки: %s",
                instances.getFirst().getChecklist().getName());
        addTableRowWithBreaks(table, checklistTitle, true);

        StringBuilder data = new StringBuilder();
        for (ChecklistInstance checklist : instances) {
            data.append("- Параметр: ").append(checklist.getParameter().getName()).append("\n");

            if (checklist.getParameterType() != null) {
                data.append("  - Тип: ").append(checklist.getParameterType().getName()).append("\n");
            }

            if (checklist.getSubtype() != null) {
                data.append("  - Подтип: ").append(checklist.getSubtype().getName()).append("\n");
            }

            if (checklist.getSpecValues() != null && !checklist.getSpecValues().isEmpty()) {
                data.append("  - Спецификации:\n");
                for (SpecValue param : checklist.getSpecValues()) {
                    if (param != null && param.getSpec() != null) {
                        String paramName = param.getSpec().getName();
                        String paramUnit = param.getSpec().getUnit() != null ? " (" + param.getSpec().getUnit() + ")" : "";
                        String paramValue = param.getValue() != null ? param.getValue() : "не указано";
                        data.append("    - ").append(paramName).append(paramUnit).append(": ").append(paramValue).append("\n");
                    }
                }
            }
            data.append("\n");
        }

        addTableRowWithBreaks(table, data.toString(), false);
        return table;
    }

    private void addTableRowWithBreaks(Tbl table, String text, boolean bold) {
        Tr row = tableUtil.createTableRow();
        Tc cell = tableUtil.createEmptyCell();
        ObjectFactory factory = new ObjectFactory();

        String[] lines = text.split("\n");
        for (String line : lines) {
            P paragraph = factory.createP();
            R run = factory.createR();
            Text textObj = factory.createText();
            textObj.setValue(line.trim());
            run.getContent().add(textObj);

            if (bold) {
                RPr runProperties = factory.createRPr();
                BooleanDefaultTrue boldProp = factory.createBooleanDefaultTrue();
                boldProp.setVal(true);
                runProperties.setB(boldProp);
                run.setRPr(runProperties);
            }

            PPr paragraphProperties = factory.createPPr();
            PPrBase.Spacing spacing = factory.createPPrBaseSpacing();
            spacing.setBefore(BigInteger.valueOf(0));
            spacing.setAfter(BigInteger.valueOf(120));
            spacing.setLine(BigInteger.valueOf(240));
            spacing.setLineRule(STLineSpacingRule.AUTO);
            paragraphProperties.setSpacing(spacing);
            paragraph.setPPr(paragraphProperties);

            paragraph.getContent().add(run);
            cell.getContent().add(paragraph);
        }

        row.getContent().add(cell);
        table.getContent().add(row);
    }
}