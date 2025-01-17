package org.example.expert.domain.user.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<Todo> findByIdWithUser(Long todoId);
    Optional<TodoResponse> findTodoDtoByIdWithUser(Long todoId);
}
