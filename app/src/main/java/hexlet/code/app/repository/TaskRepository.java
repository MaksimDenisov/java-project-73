package hexlet.code.app.repository;

import hexlet.code.app.dto.TaskTO;
import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task> {
}
