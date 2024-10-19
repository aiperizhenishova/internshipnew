package kg.alatoo.taskmanagementsystem.repositories;

import kg.alatoo.taskmanagementsystem.entities.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerRepository extends JpaRepository<WorkerEntity, Long> {

    List<WorkerEntity> findAllByFullNameContainingIgnoreCase(String name);

}
