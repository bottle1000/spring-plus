package org.example.expert.domain.user.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<Todo> findByIdWithUser(Long todoId);
    Optional<TodoResponse> findTodoDtoByIdWithUser(Long todoId);

    Page<TodoSearchResponse> findTodoByTitleAndDateAndNickname(TodoSearchRequest request, Pageable pageable);
}
