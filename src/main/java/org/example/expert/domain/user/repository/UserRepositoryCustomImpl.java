package org.example.expert.domain.user.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QTodo todo = QTodo.todo;
    QUser user = QUser.user;
    QManager manager = QManager.manager;
    QComment comment = QComment.comment;

    // 1. 바로 엔티티를 반환해주는 쿼리문
    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    // 1번에서 엔티티를 반환 하는 것을 DTO로 반환시켜주기 위한 쿼리문
    @Override
    public Optional<TodoResponse> findTodoDtoByIdWithUser(Long todoId) {
        TodoResponse result = queryFactory.select(Projections.constructor(
                        TodoResponse.class,
                        todo.id,
                        todo.title,
                        todo.contents,
                        todo.weather,
                        Projections.constructor(UserResponse.class,
                                user.id,
                                user.email),
                        todo.createdAt,
                        todo.modifiedAt
                ))
                .from(todo)
                .leftJoin(todo.user, user)
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> findTodoByTitleAndDateAndNickname(TodoSearchRequest request, Pageable pageable) {
        List<TodoSearchResponse> result = queryFactory.select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()
                ))
                .from(todo)
                .where(todo.title.contains(request.getTitle()),
                        todo.createdAt.between(request.getStartDate(), request.getEndDate()),
                        user.nickname.contains(request.getManagerNickname())
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(todo)
                .where(todo.title.contains(request.getTitle()),
                        todo.createdAt.between(request.getStartDate(), request.getEndDate()),
                        user.nickname.contains(request.getManagerNickname())
                )
                .fetchCount();

        return new PageImpl<>(result, pageable, total);
    }


}
