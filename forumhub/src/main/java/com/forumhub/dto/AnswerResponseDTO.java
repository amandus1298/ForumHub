package com.forumhub.dto;

import java.time.LocalDateTime;

public record AnswerResponseDTO(Long id, String topicName, String author, String content, LocalDateTime createdAt) {
}
