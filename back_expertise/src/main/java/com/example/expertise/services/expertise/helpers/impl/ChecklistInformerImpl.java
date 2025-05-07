package com.example.expertise.services.expertise.helpers.impl;

import com.example.expertise.model.checklist.*;
import com.example.expertise.repository.checklist.*;
import com.example.expertise.services.expertise.helpers.ChecklistInformer;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Сервис для получения информации о чек-листах
 */
@Component
public class ChecklistInformerImpl implements ChecklistInformer {

    private final ParameterRepository parameterRepository;
    private final ParameterTypeRepository parameterTypeRepository;
    private final SubtypeRepository subtypeRepository;
    private final ParameterSpecRepository parameterSpecRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistInstanceRepository checklistInstanceRepository;
    private final ChecklistFieldRepository checklistFieldRepository;

    public ChecklistInformerImpl(ParameterRepository parameterRepository,
                                 ParameterTypeRepository parameterTypeRepository,
                                 SubtypeRepository subtypeRepository,
                                 ParameterSpecRepository parameterSpecRepository,
                                 ChecklistRepository checklistRepository,
                                 ChecklistInstanceRepository checklistInstanceRepository,
                                 ChecklistFieldRepository checklistFieldRepository) {
        this.parameterRepository = parameterRepository;
        this.parameterTypeRepository = parameterTypeRepository;
        this.subtypeRepository = subtypeRepository;
        this.parameterSpecRepository = parameterSpecRepository;
        this.checklistRepository = checklistRepository;
        this.checklistInstanceRepository = checklistInstanceRepository;
        this.checklistFieldRepository = checklistFieldRepository;
    }

    /**
     * Получить чек-лист (верхний уровень) по идентификатору
     *
     * @param checklistId идентификатор чек-листа
     * @return объект чек-листа
     */
    @Override
    public Checklist getChecklist(@NotNull UUID checklistId) {
        return checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found"));
    }

    /**
     * Получить параметр по идентификатору
     *
     * @param parameterId идентификатор параметра
     * @return объект параметра
     */
    @Override
    public Parameter getParameter(@NotNull UUID parameterId) {
        return parameterRepository.findById(parameterId)
                .orElseThrow(() -> new RuntimeException("Parameter not found"));
    }

    /**
     * Получить тип параметра по идентификатору
     *
     * @param typeId идентификатор типа параметра
     * @return объект типа параметра
     */
    @Override
    public ParameterType getParameterType(@NotNull UUID typeId) {
        return parameterTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Parameter type not found"));
    }

    /**
     * Получить подтип по идентификатору
     *
     * @param subtypeId идентификатор подтипа
     * @return объект подтипа
     */
    @Override
    public Subtype getSubtype(@NotNull UUID subtypeId) {
        return subtypeRepository.findById(subtypeId)
                .orElseThrow(() -> new RuntimeException("Subtype not found"));
    }

    /**
     * Получить спецификацию параметра по идентификатору
     *
     * @param specId идентификатор спецификации
     * @return объект спецификации
     */
    @Override
    public ParameterSpec getParameterSpec(@NotNull UUID specId) {
        return parameterSpecRepository.findById(specId)
                .orElseThrow(() -> new RuntimeException("Parameter spec not found"));
    }

    /**
     * Получить экземпляр чек-листа по идентификатору
     *
     * @param instanceId идентификатор экземпляра чек-листа
     * @return экземпляр чек-листа
     */
    @Override
    public ChecklistInstance getChecklistInstance(@NotNull UUID instanceId) {
        return checklistInstanceRepository.findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Checklist instance not found"));
    }

    @Override
    public ChecklistField getChecklistField(UUID checklistFieldId) {
        return checklistFieldRepository.findById(checklistFieldId)
                .orElseThrow(() -> new RuntimeException("Checklist field not found"));
    }
}
