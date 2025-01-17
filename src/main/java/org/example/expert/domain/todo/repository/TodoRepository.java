package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    @Query("SELECT t " +
            "FROM Todo t " +
            "WHERE t.weather = :weather " +
            "AND t.modifiedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodoByWeatherAndModifiedAt(LocalDateTime startDate, LocalDateTime endDate, String weather, Pageable pageable);


    @Query("SELECT t " +
            "FROM Todo t " +
            "WHERE t.weather = :weather " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodoByWeather(@Param("weather") String weather, Pageable pageable);


    @Query("SELECT t " +
            "FROM Todo t " +
            "WHERE t.modifiedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodoByModifiedAt(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
