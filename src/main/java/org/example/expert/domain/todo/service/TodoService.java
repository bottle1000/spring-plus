package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional(readOnly = false)
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    public Page<TodoResponse> getTodos(LocalDateTime startDate,
                                       LocalDateTime endDate,
                                       String weather,
                                       int page,
                                       int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todos;

        if (weather != null && startDate != null && endDate != null) {
            // weather 조건과 기간 조건이 모두 있는 경우
            todos = todoRepository.findTodoByWeatherAndModifiedAt(startDate, endDate, weather, pageable);
        } else if (weather != null) {
            // weather 조건만 있는 경우
            todos = todoRepository.findTodoByWeather(weather, pageable);
        } else if (startDate != null && endDate != null) {
            // 기간 조건만 있는 경우
            todos = todoRepository.findTodoByModifiedAt(startDate, endDate, pageable);
        } else {
            // 모든 조건이 없는 경우
            todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);
        }



        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {

        return todoRepository.findTodoDtoByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));
    }
}
