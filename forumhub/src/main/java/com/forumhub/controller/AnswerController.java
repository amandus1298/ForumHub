package com.forumhub.controller;

import com.forumhub.dto.AnswerRequestDTO;
import com.forumhub.dto.AnswerResponseDTO;
import com.forumhub.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics/{topicId}/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private TopicController topicController;
    @PostMapping
    public ResponseEntity<?> postAnswer(
                                @PathVariable Long topicId,
                                @RequestBody AnswerRequestDTO dto,
                                Authentication authentication) {
        if(topicId == null || topicId <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("nao encontrado");
        }
        String email = authentication.getName();
        answerService.addAnswer(topicId, dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Resposta adicionada com sucesso.");
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponseDTO>> listAnswers(@PathVariable Long topicId) {

        List<AnswerResponseDTO> responses = answerService.listAnswers(topicId);
        return ResponseEntity.ok(responses);
    }
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long topicId, @PathVariable Long answerId, Authentication authentication) {
        String email = authentication.getName();
        answerService.deleteReply(topicId,answerId, email);
        return ResponseEntity.ok("Resposta deletada com sucesso");
    }
}