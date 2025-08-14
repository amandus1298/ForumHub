package com.forumhub.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TopicResponseDTO(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt,
        List<AnswerResponseDTO> answers
) {}