package com.example.planner.todo.repositories;

import com.example.planner.entity.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByUserIdOrderByIdAsc(Long id);

    @Query("SELECT p FROM Priority p where " +
            "(:title is null or :title='' " +
            " or lower(p.title) like lower(concat('%', :title,'%'))) " +
            " and p.userId=:id " +
            "order by p.title asc")
    List<Priority> findByTitle(@Param("title") String title, @Param("id") Long id);

}
