package app.repositories;

import app.entities.Task;
import org.springframework.data.repository.CrudRepository;


public interface TaskRepository
        extends CrudRepository<Task, Long> {
}