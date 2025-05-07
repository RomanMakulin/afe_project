package com.example.expertise.services.expertise.helpers;

import com.example.expertise.model.checklist.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Сервис для получения информации о чек-листах
 */
public interface ChecklistInformer {
    /**
     * Получить чек-лист (верхний уровень) по идентификатору
     *
     * @param checklistId идентификатор чек-листа
     * @return объект чек-листа
     */
    Checklist getChecklist(@NotNull UUID checklistId);

    /**
     * Получить параметр по идентификатору
     *
     * @param parameterId идентификатор параметра
     * @return объект параметра
     */
    Parameter getParameter(@NotNull UUID parameterId);

    /**
     * Получить тип параметра по идентификатору
     *
     * @param typeId идентификатор типа параметра
     * @return объект типа параметра
     */
    ParameterType getParameterType(@NotNull UUID typeId);

    /**
     * Получить подтип по идентификатору
     *
     * @param subtypeId идентификатор подтипа
     * @return объект подтипа
     */
    Subtype getSubtype(@NotNull UUID subtypeId);

    /**
     * Получить спецификацию параметра по идентификатору
     *
     * @param specId идентификатор спецификации
     * @return объект спецификации
     */
    ParameterSpec getParameterSpec(@NotNull UUID specId);

    /**
     * Получить экземпляр чек-листа по идентификатору
     *
     * @param instanceId идентификатор экземпляра чек-листа
     * @return экземпляр чек-листа
     */
    ChecklistInstance getChecklistInstance(@NotNull UUID instanceId);

    ChecklistField getChecklistField(@NotNull UUID checklistFieldId);
}
