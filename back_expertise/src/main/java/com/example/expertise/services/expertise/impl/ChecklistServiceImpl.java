package com.example.expertise.services.expertise.impl;

import com.example.expertise.dto.checklist.CreateChecklistInstanceDto;
import com.example.expertise.dto.checklist.SaveChecklistInstanceFieldDto;
import com.example.expertise.dto.checklist.SaveParameterValueDto;
import com.example.expertise.model.checklist.*;
import com.example.expertise.model.expertise.ExpertiseQuestion;
import com.example.expertise.repository.checklist.*;
import com.example.expertise.services.expertise.ChecklistService;
import com.example.expertise.services.expertise.ExpertiseQuestionService;
import com.example.expertise.services.expertise.helpers.ChecklistInformer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с чек-листами и параметрами
 */
@Validated
@Service
public class ChecklistServiceImpl implements ChecklistService {

    private static final Logger log = LoggerFactory.getLogger(ChecklistServiceImpl.class);

    private final ChecklistRepository checklistRepository;
    private final ChecklistInstanceRepository checklistInstanceRepository;
    private final ChecklistFieldRepository checklistFieldRepository; // Новый репозиторий
    private final ChecklistInstanceFieldRepository checklistInstanceFieldRepository; // Новый репозиторий
    private final ExpertiseQuestionService expertiseQuestionService;
    private final ChecklistInformer checklistInformer;

    public ChecklistServiceImpl(ChecklistRepository checklistRepository,
                                ChecklistInstanceRepository checklistInstanceRepository,
                                ChecklistFieldRepository checklistFieldRepository,
                                ChecklistInstanceFieldRepository checklistInstanceFieldRepository,
                                ExpertiseQuestionService expertiseQuestionService,
                                ChecklistInformer checklistInformer) {
        this.checklistRepository = checklistRepository;
        this.checklistInstanceRepository = checklistInstanceRepository;
        this.checklistFieldRepository = checklistFieldRepository;
        this.checklistInstanceFieldRepository = checklistInstanceFieldRepository;
        this.expertiseQuestionService = expertiseQuestionService;
        this.checklistInformer = checklistInformer;
    }

    @Override
    public List<Checklist> getAllTopLevelChecklists() {
        return checklistRepository.findAll();
    }

    @Override
    public List<ChecklistField> getChecklistFields(UUID checklistId) {
        Checklist checklist = checklistInformer.getChecklist(checklistId);
        return checklist.getChecklistFields() != null ? checklist.getChecklistFields() : Collections.emptyList();
    }

    @Override
    public List<Parameter> getParametersByChecklistId(@NotNull UUID checklistId) {
        Checklist checklist = checklistInformer.getChecklist(checklistId);
        return checklist.getChecklistParameters() != null
                ? checklist.getChecklistParameters().stream().map(ChecklistParameter::getParameter).toList()
                : Collections.emptyList();
    }

    @Override
    public List<ParameterType> getParameterTypes(@NotNull UUID parameterId) {
        Parameter parameter = checklistInformer.getParameter(parameterId);
        return parameter.getParameterTypes() != null ? parameter.getParameterTypes() : Collections.emptyList();
    }

    @Override
    public List<Subtype> getParameterSubtypes(@NotNull UUID typeId) {
        ParameterType parameterType = checklistInformer.getParameterType(typeId);
        return parameterType.getSubtypes() != null ? parameterType.getSubtypes() : Collections.emptyList();
    }

    @Override
    public ChecklistInstance createChecklistInstance(@NotNull @Valid CreateChecklistInstanceDto createChecklistInstanceData) {
        Checklist checklist = checklistInformer.getChecklist(createChecklistInstanceData.getChecklistId());
        Parameter parameter = checklistInformer.getParameter(createChecklistInstanceData.getParameterId());
        ParameterType parameterType = checklistInformer.getParameterType(createChecklistInstanceData.getParameterTypeId());
        Subtype subtype = createChecklistInstanceData.getSubtypeId() != null ?
                checklistInformer.getSubtype(createChecklistInstanceData.getSubtypeId()) : null;
        ExpertiseQuestion expertiseQuestion = expertiseQuestionService.getExpertiseQuestionById(
                createChecklistInstanceData.getExpertiseQuestionId());

        ChecklistInstance checklistInstance = new ChecklistInstance(checklist, parameter, parameterType, subtype);
        checklistInstance.setExpertiseQuestion(expertiseQuestion); // Устанавливаем связь

        // Добавляем в список, если он уже существует
        List<ChecklistInstance> instances = expertiseQuestion.getChecklistInstances();
        if (instances == null) {
            instances = new ArrayList<>();
            expertiseQuestion.setChecklistInstances(instances);
        }
        instances.add(checklistInstance);

        try {
            ChecklistInstance savedInstance = checklistInstanceRepository.save(checklistInstance);
            log.info("Checklist instance created successfully: {}", savedInstance.getId());
            return savedInstance;
        } catch (DataAccessException e) {
            log.error("Error creating checklist instance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create checklist instance: " + e.getMessage());
        }
    }

    @Override
    public List<ParameterTypeSpec> getSpecsByTypeId(@NotNull UUID typeId) {
        ParameterType parameterType = checklistInformer.getParameterType(typeId);
        return parameterType.getParameterTypeSpecs() != null ? parameterType.getParameterTypeSpecs() : Collections.emptyList();
    }

    @Override
    public ChecklistInstance saveSpecValues(@NotNull @NotEmpty @Valid List<SaveParameterValueDto> saveParameterValueData) {
        // Получаем checklistInstance по первому элементу
        UUID checklistInstanceId = saveParameterValueData.getFirst().getChecklistInstanceId();
        ChecklistInstance checklistInstance = checklistInformer.getChecklistInstance(checklistInstanceId);

        // Проверяем, что все элементы относятся к одному checklistInstance
        boolean allMatch = saveParameterValueData.stream().allMatch(data -> data.getChecklistInstanceId().equals(checklistInstanceId));
        if (!allMatch) {
            throw new IllegalArgumentException("All spec values must belong to the same checklist instance");
        }

        // Создаем список SpecValue
        List<SpecValue> specValueList = new ArrayList<>();
        saveParameterValueData.forEach(itemData -> {
            ParameterSpec spec = checklistInformer.getParameterSpec(itemData.getSpecId());
            SpecValue specValue = new SpecValue(spec, itemData.getValue());
            specValue.setChecklistInstance(checklistInstance); // Устанавливаем связь
            specValueList.add(specValue);
        });
        // Устанавливаем список и сохраняем
        checklistInstance.setSpecValues(specValueList);

        try {
            ChecklistInstance savedInstance = checklistInstanceRepository.save(checklistInstance);
            log.info("Spec values saved successfully for checklist instance: {}", savedInstance.getId());
            return savedInstance;
        } catch (DataAccessException e) {
            log.error("Error saving spec values: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save spec values: " + e.getMessage());
        }
    }

    @Override
    public ChecklistInstance saveChecklistInstanceFields(List<SaveChecklistInstanceFieldDto> saveFieldValuesData) {
        // Получаем checklistInstance по первому элементу
        UUID checklistInstanceId = saveFieldValuesData.getFirst().getChecklistInstanceId();
        ChecklistInstance checklistInstance = checklistInformer.getChecklistInstance(checklistInstanceId);

        // Проверяем, что все элементы относятся к одному checklistInstance
        boolean allMatch = saveFieldValuesData.stream()
                .allMatch(data -> data.getChecklistInstanceId().equals(checklistInstanceId));
        if (!allMatch) {
            throw new IllegalArgumentException("All field values must belong to the same checklist instance");
        }

        // Получаем текущий список полей экземпляра (если он уже есть)
        List<ChecklistInstanceField> existingFields = checklistInstance.getChecklistInstanceFields();
        if (existingFields == null) {
            existingFields = new ArrayList<>();
            checklistInstance.setChecklistInstanceFields(existingFields);
        }

        // Мапа для быстрого доступа к существующим полям по checklistFieldId
        Map<UUID, ChecklistInstanceField> existingFieldsMap = existingFields.stream()
                .collect(Collectors.toMap(
                        field -> field.getChecklistField().getId(),
                        field -> field
                ));

        // Обрабатываем входные данные с использованием обычного цикла
        for (SaveChecklistInstanceFieldDto itemData : saveFieldValuesData) {
            UUID checklistFieldId = itemData.getChecklistFieldId();
            ChecklistField field = checklistInformer.getChecklistField(checklistFieldId);

            // Проверяем, существует ли запись для данного checklistFieldId
            ChecklistInstanceField existingField = existingFieldsMap.get(checklistFieldId);
            if (existingField != null) {
                // Если запись существует, обновляем значение
                existingField.setValue(itemData.getValue());
            } else {
                // Если записи нет, создаем новую
                ChecklistInstanceField newField = new ChecklistInstanceField(checklistInstance, field, itemData.getValue());
                existingFields.add(newField);
                existingFieldsMap.put(checklistFieldId, newField); // Добавляем в мапу для последующих проверок
            }
        }

        // Сохраняем изменения
        try {
            ChecklistInstance savedInstance = checklistInstanceRepository.save(checklistInstance);
            log.info("Checklist instance fields saved successfully for checklist instance: {}", savedInstance.getId());
            return savedInstance;
        } catch (DataAccessException e) {
            log.error("Error saving checklist instance fields: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save checklist instance fields: " + e.getMessage());
        }
    }

    @Override
    public ChecklistInstance getChecklistInstanceById(@NotNull UUID instanceId) {
        return checklistInformer.getChecklistInstance(instanceId);
    }
}