package kg.alatoo.taskmanagementsystem.repositories;

import kg.alatoo.taskmanagementsystem.entities.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AdminEntity, Long> {

    Optional<AdminEntity> findByUsername(String username);
}
