package com.example.expertise.controller;

import com.example.expertise.dto.expertise.AnswerDto;
import com.example.expertise.dto.expertise.ExpertiseQuestionDto;
import com.example.expertise.model.expertise.ExpertiseQuestion;
import com.example.expertise.services.expertise.ExpertiseQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/expertise/question")
public class ExpertiseQuestionController {

    private final ExpertiseQuestionService expertiseQuestionService;

    public ExpertiseQuestionController(ExpertiseQuestionService expertiseQuestionService) {
        this.expertiseQuestionService = expertiseQuestionService;
    }

    /**
     * Получить конкретный вопрос экспертизы по id
     *
     * @param id идентификатор вопроса экспертизы
     * @return 200 OK с DTO вопроса
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ExpertiseQuestionDto> getExpertiseQuestion(@PathVariable UUID id) {
        ExpertiseQuestion question = expertiseQuestionService.getExpertiseQuestionById(id);
        return ResponseEntity.ok(new ExpertiseQuestionDto(question));
    }

    /**
     * Создать ответ на вопрос экспертизы
     *
     * @param answerDto данные ответа на вопрос экспертизы
     * @return 200 OK с обновлённым DTO вопроса
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExpertiseQuestionDto> createAnswer(@Valid @ModelAttribute AnswerDto answerDto) {
        ExpertiseQuestion question = expertiseQuestionService.createAnswer(answerDto);
        return ResponseEntity.ok(new ExpertiseQuestionDto(question));
    }

    /**
     * Обновить резюмирующий текст вопроса ответа экспертизы (вывод)
     * @param conclusion текст вывода ответа на вопрос экспертизы
     * @param questionId идентификатор вопроса экспертизы
     * @return 200 OK с обновлённым DTO вопроса
     */
    @PostMapping("/update-conclusion")
    public ResponseEntity<ExpertiseQuestionDto> updateConclusion(@RequestParam String conclusion, @RequestParam UUID questionId) {
        ExpertiseQuestion question = expertiseQuestionService.updateAnswerConclusion(conclusion, questionId);
        return ResponseEntity.ok(new ExpertiseQuestionDto(question));
    }

    /**
     * Удалить фото вопроса ответа экспертизы
     *
     * @param filePath имя файла фото ответа на вопрос экспертизы
     * @return 200 OK с обновлённым DTO вопроса
     */
    @DeleteMapping("/delete-photo/{filePath}")
    public ResponseEntity<ExpertiseQuestionDto> deletePhoto(@PathVariable String filePath) {
        ExpertiseQuestion question = expertiseQuestionService.deletePhotoByName(filePath);
        return ResponseEntity.ok(new ExpertiseQuestionDto(question));
    }
}