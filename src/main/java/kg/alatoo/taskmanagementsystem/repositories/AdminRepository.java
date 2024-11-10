package kg.alatoo.taskmanagementsystem.repositories;

import kg.alatoo.taskmanagementsystem.entities.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    Optional<AdminEntity> findByUsername(String username);

    List<AdminEntity> findAllByNameContainingIgnoreCase(String name);
}
