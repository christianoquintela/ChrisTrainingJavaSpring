package com.example.training.repositories;

import com.example.training.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // busca com a ferramenta do Jpa a comumente usada nas apis springboot
    List<Task> findByUser_Id(Long id);

    //Busca usando a ferramenta do Springboot
    //@Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    //List<Task> findByUser_Id(@Param ("id") Long id);

    //Busca usando select original do mysql ou postgres ou o banco que estiver usando
//    @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
//    List<Task> findByUser_Id(@Param("id") Long id);
}
