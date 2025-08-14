package com.forumhub.service;

import com.forumhub.dto.AnswerRequestDTO;
import com.forumhub.dto.AnswerResponseDTO;
import com.forumhub.entity.Answer;
import com.forumhub.entity.Topic;
import com.forumhub.entity.User;
import com.forumhub.repository.AnswerRepository;
import com.forumhub.repository.TopicRepository;
import com.forumhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public Answer addAnswer(Long topicId, AnswerRequestDTO dto, String email) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Answer answer = new Answer();
        answer.setContent(dto.content());
        answer.setAuthor(author);
        answer.setTopic(topic);
        answer.setCreatedAt(LocalDateTime.now());

        return answerRepository.save(answer);
    }

    public List<AnswerResponseDTO> listAnswers(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));

        return topic.getAnswers().stream()
                .map(answer -> new AnswerResponseDTO(
                        answer.getId(),
                        answer.getTopic().getTitle(),
                        answer.getAuthor().getName(),
                        answer.getContent(),
                        answer.getCreatedAt()
                ))
                .toList();
    }

    public void deleteReply(Long topicId, Long answerId, String email) {
        Answer reply = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Resposta não encontrada"));
        if (!reply.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Você não tem permissão para deletar esta resposta");
        }
        if (!reply.getTopic().getId().equals(topicId)) {
            throw new RuntimeException("Esta resposta não pertence ao tópico informado");
        }


        answerRepository.delete(reply);
    }
}
