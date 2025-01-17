package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoSearchRequest {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String managerNickname;
}
