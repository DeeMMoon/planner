package com.example.planner.todo.repositories;

import com.example.planner.entity.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdOrderByTitleAsc(Long id);

    @Query("SELECT c FROM Category c where " +
            "(:title is null or :title='' " +
            " or lower(c.title) like lower(concat('%', :title,'%'))) " +
            " and c.userId=:id  " +
            " order by c.title asc")

    List<Category> findByTitle(@Param("title") String title, @Param("id") Long id);
}
