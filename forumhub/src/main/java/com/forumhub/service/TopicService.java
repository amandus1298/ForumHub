package com.forumhub.service;

import com.forumhub.dto.AnswerResponseDTO;
import com.forumhub.dto.TopicRequestDTO;
import com.forumhub.dto.TopicResponseDTO;
import com.forumhub.entity.Topic;
import com.forumhub.entity.User;
import com.forumhub.repository.TopicRepository;
import com.forumhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    public Topic createTopic(TopicRequestDTO dto, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Topic topic = new Topic();
        topic.setTitle(dto.title());
        topic.setContent(dto.content());
        topic.setAuthor(author);
        topic.setCreatedAt(LocalDateTime.now());

        return topicRepository.save(topic);
    }

    public List<TopicResponseDTO> listTopics() {
        return topicRepository.findAll().stream()
                .map(topic -> new TopicResponseDTO(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getContent(),
                        topic.getAuthor().getName(), // ou .getEmail()
                        topic.getCreatedAt(),
                        topic.getAnswers().stream()
                                .map(reply -> new AnswerResponseDTO(
                                reply.getId(),
                                reply.getTopic().getTitle(),
                                reply.getAuthor().getName(),
                                reply.getContent(),
                                reply.getCreatedAt()
                        )).toList()
                ))
                .toList();
    }

    public void deleteTopic(Long topicId, String email) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));

        if (!topic.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Você não tem permissão para deletar este tópico");
        }

        topicRepository.delete(topic);
    }
}