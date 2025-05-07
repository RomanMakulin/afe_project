package com.example.expertise.controller;

import com.example.expertise.dto.checklist.*;
import com.example.expertise.model.checklist.ChecklistInstance;
import com.example.expertise.model.checklist.ParameterTypeSpec;
import com.example.expertise.services.expertise.ChecklistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с чек-листами и параметрами.
 */
@RestController
@RequestMapping("/api/expertise")
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    /**
     * Возвращает список всех чек-листов (верхний уровень) с id и name.
     *
     * @return список чек-листов
     */
    @GetMapping("/checklists")
    public ResponseEntity<List<ChecklistSimpleDto>> getAllChecklists() {
        List<ChecklistSimpleDto> checklists = checklistService.getAllTopLevelChecklists()
                .stream()
                .map(ChecklistSimpleDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(checklists);
    }

    /**
     * Возвращает список полей чек-листа по его идентификатору.
     *
     * @param checklistId идентификатор чек-листа
     * @return список полей
     */
    @GetMapping("/checklists/{checklistId}/fields")
    public ResponseEntity<List<ChecklistFieldDto>> getChecklistFields(@PathVariable UUID checklistId) {
        List<ChecklistFieldDto> fields = checklistService.getChecklistFields(checklistId)
                .stream()
                .map(ChecklistFieldDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fields);
    }

    /**
     * Сохраняет значения полей экземпляра чек-листа.
     *
     * @param saveFieldValuesDto данные для сохранения значений полей
     * @return обновленный экземпляр чек-листа
     */
    @PostMapping("/checklist-instance-fields")
    public ResponseEntity<ChecklistInstanceResponseDto> saveChecklistInstanceFields(
            @Valid @RequestBody List<SaveChecklistInstanceFieldDto> saveFieldValuesDto) {
        ChecklistInstance instance = checklistService.saveChecklistInstanceFields(saveFieldValuesDto);
        return ResponseEntity.ok(new ChecklistInstanceResponseDto(instance));
    }

    /**
     * Возвращает список параметров, связанных с чек-листом, с id и name.
     *
     * @param checklistId идентификатор чек-листа
     * @return список параметров
     */
    @GetMapping("/checklists/{checklistId}/parameters")
    public ResponseEntity<List<ParameterSimpleDto>> getParametersByChecklistId(@PathVariable UUID checklistId) {
        List<ParameterSimpleDto> parameters = checklistService.getParametersByChecklistId(checklistId)
                .stream()
                .map(ParameterSimpleDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(parameters);
    }

    /**
     * Возвращает список типов параметра по его идентификатору с id и name.
     *
     * @param parameterId идентификатор параметра
     * @return список типов параметра
     */
    @GetMapping("/parameters/{parameterId}/types")
    public ResponseEntity<List<ParameterTypeSimpleDto>> getParameterTypes(@PathVariable UUID parameterId) {
        List<ParameterTypeSimpleDto> types = checklistService.getParameterTypes(parameterId)
                .stream()
                .map(ParameterTypeSimpleDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(types);
    }

    /**
     * Возвращает список подтипов параметра по идентификатору типа с id и name.
     *
     * @param typeId идентификатор типа параметра
     * @return список подтипов
     */
    @GetMapping("/parameter-types/{typeId}/subtypes")
    public ResponseEntity<List<SubtypeSimpleDto>> getParameterSubtypes(@PathVariable UUID typeId) {
        List<SubtypeSimpleDto> subtypes = checklistService.getParameterSubtypes(typeId)
                .stream()
                .map(SubtypeSimpleDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subtypes);
    }

    /**
     * Создает новый экземпляр чек-листа.
     *
     * @param createChecklistInstanceDto данные для создания экземпляра чек-листа
     * @return созданный экземпляр чек-листа
     */
    @PostMapping("/checklist-instances")
    public ResponseEntity<ChecklistInstanceResponseDto> createChecklistInstance(
            @Valid @RequestBody CreateChecklistInstanceDto createChecklistInstanceDto) {
        ChecklistInstance instance = checklistService.createChecklistInstance(createChecklistInstanceDto);
        return ResponseEntity.ok(new ChecklistInstanceResponseDto(instance));
    }

    /**
     * Возвращает список спецификаций параметра по идентификатору типа.
     *
     * @param typeId идентификатор типа параметра
     * @return список спецификаций
     */
    @GetMapping("/parameter-types/{typeId}/specs")
    public ResponseEntity<List<ParameterTypeSpec>> getSpecsByTypeId(@PathVariable UUID typeId) {
        return ResponseEntity.ok(checklistService.getSpecsByTypeId(typeId));
    }

    /**
     * Сохраняет значения спецификаций чек-листа.
     *
     * @param saveParameterValueData данные для сохранения значений спецификаций
     * @return обновленный экземпляр чек-листа
     */
    @PostMapping("/spec-values")
    public ResponseEntity<ChecklistInstanceResponseDto> saveSpecValues(
            @RequestBody List<SaveParameterValueDto> saveParameterValueData) {
        ChecklistInstance instance = checklistService.saveSpecValues(saveParameterValueData);
        return ResponseEntity.ok(new ChecklistInstanceResponseDto(instance));
    }

    /**
     * Возвращает экземпляр чек-листа по его идентификатору.
     *
     * @param instanceId идентификатор экземпляра чек-листа
     * @return экземпляр чек-листа
     */
    @GetMapping("/checklist-instances/{instanceId}")
    public ResponseEntity<ChecklistInstanceResponseDto> getChecklistInstance(@PathVariable UUID instanceId) {
        ChecklistInstance instance = checklistService.getChecklistInstanceById(instanceId);
        return ResponseEntity.ok(new ChecklistInstanceResponseDto(instance));
    }
}