package com.example.expertise.services.expertise;

import com.example.expertise.dto.checklist.ChecklistFieldDto;
import com.example.expertise.dto.checklist.CreateChecklistInstanceDto;
import com.example.expertise.dto.checklist.SaveChecklistInstanceFieldDto;
import com.example.expertise.dto.checklist.SaveParameterValueDto;
import com.example.expertise.model.checklist.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с чек-листами и параметрами
 */
public interface ChecklistService {

    /**
     * Получить список всех чек-листов (верхний уровень)
     *
     * @return список всех чек-листов
     */
    List<Checklist> getAllTopLevelChecklists();

    /**
     * Получить список параметров, связанных с чек-листом
     *
     * @param checklistId идентификатор чек-листа, не может быть null
     * @return список параметров
     */
    List<Parameter> getParametersByChecklistId(@NotNull UUID checklistId);

    /**
     * Получить список типов для конкретного параметра
     *
     * @param parameterId идентификатор параметра, не может быть null
     * @return список типов параметра
     */
    List<ParameterType> getParameterTypes(@NotNull UUID parameterId);

    /**
     * Получить список подтипов для конкретного типа параметра
     *
     * @param typeId идентификатор типа параметра, не может быть null
     * @return список подтипов
     */
    List<Subtype> getParameterSubtypes(@NotNull UUID typeId);

    /**
     * Создать экземпляр чек-листа
     *
     * @param createChecklistInstanceDto данные для создания экземпляра чек-листа, не могут быть null
     * @return созданный экземпляр чек-листа
     */
    ChecklistInstance createChecklistInstance(@NotNull @Valid CreateChecklistInstanceDto createChecklistInstanceDto);

    /**
     * Получить список спецификаций для типа параметра
     *
     * @param typeId идентификатор типа параметра, не может быть null
     * @return список спецификаций типа параметра
     */
    List<ParameterTypeSpec> getSpecsByTypeId(@NotNull UUID typeId);

    /**
     * Сохранить значения спецификаций для экземпляра чек-листа
     *
     * @param saveParameterValueData список данных для сохранения значений спецификаций, не может быть null или пустым
     * @return обновленный экземпляр чек-листа
     */
    ChecklistInstance saveSpecValues(@NotNull @NotEmpty @Valid List<SaveParameterValueDto> saveParameterValueData);

    /**
     * Получить экземпляр чек-листа по идентификатору
     *
     * @param instanceId идентификатор экземпляра чек-листа, не может быть null
     * @return экземпляр чек-листа
     */
    ChecklistInstance getChecklistInstanceById(@NotNull UUID instanceId);

    /**
     * Сохранить значения полей экземпляра чек-листа
     *
     * @param saveFieldValuesDto список полей экземпляра чек-листа, не может быть null или пустым
     * @return обновленный экземпляр чек-листа
     */
    ChecklistInstance saveChecklistInstanceFields(List<SaveChecklistInstanceFieldDto> saveFieldValuesDto);

    /**
     * Получить поля экземпляра чек-листа
     * @param checklistId идентификатор чек-листа, не может быть null
     * @return список полей экземпляра чек-листа
     */
    List<ChecklistField> getChecklistFields(UUID checklistId);
}