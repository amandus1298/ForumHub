package com.forumhub.controller;


import com.forumhub.dto.TopicRequestDTO;
import com.forumhub.dto.TopicResponseDTO;
import com.forumhub.entity.Topic;
import com.forumhub.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping("/create")
    public ResponseEntity<?> createTopic(@RequestBody TopicRequestDTO dto, Authentication authentication) {


        String email = authentication.getName(); // vem do JWT
        if(!authentication.isAuthenticated()){
            System.out.println("usuario nao autenticado");
        }
        Topic topic = topicService.createTopic(dto, email);

        return ResponseEntity.status(HttpStatus.CREATED).body("Tópico criado com ID: " + topic.getId());
    }

    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> listTopics() {
        List<TopicResponseDTO> topics = topicService.listTopics();
        return ResponseEntity.ok(topics);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        topicService.deleteTopic(id, email);
        return ResponseEntity.ok("Tópico deletado com sucesso");
    }
}
